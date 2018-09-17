package dl.coursera.week2

import ch.systemsx.cisd.hdf5.{HDF5Factory, IHDF5Reader}
import dl.coursera.week2.ImageRecognitionExercise.initializeWithZeros
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4s.Implicits._
import org.nd4j.linalg.ops.transforms.Transforms._
import ImageRecognitionExercise._
import scala.annotation.tailrec

/**
  * Created by Satheesh Akkinepally on 9/09/18.
  */
class ImageRecognitionExercise(val trainingSetXOrig:INDArray,val trainingSetY:INDArray,
                               val testSetXOrig:INDArray,testSetY:INDArray,val labels:Array[String]) {

  private def getImageNDArray(index:Int):INDArray = {
    val img = trainingSetXOrig(index,->)
    img
  }

  private def mTrainingExamples:Int = {
    trainingSetXOrig.shape()(0)
  }
  private def numberOfPixelsForImage:Int = {
    trainingSetXOrig.shape()(1)
  }
  private def mTestExamples:Int = {
    testSetXOrig.shape()(0)
  }
  private def flattenedTrainingSetX:INDArray = {
    val flattened = trainingSetXOrig.reshape(trainingSetXOrig.shape()(0),-1).transpose()
    standardizedX(flattened)
  }
  private def flattenedTestSetX:INDArray = {
    val flattened = testSetXOrig.reshape(testSetXOrig.shape()(0),-1).transpose()
    standardizedX(flattened)
  }

  private def standardizedX(toStandardize:INDArray):INDArray = {
    toStandardize/255
  }

  def buildAndRunModel(numIterations:Int,learningRate:Double,printAtEachIteration:Boolean):Unit = {
    val (w,b) = initializeWithZeros(flattenedTrainingSetX.shape()(0))
    val optimized = optimize(w,b,flattenedTrainingSetX,trainingSetY,numIterations,learningRate,printAtEachIteration)
    val wPrime = optimized._1
    val bPrime = optimized._2
    val ypredictionTest = predict(wPrime,bPrime,flattenedTestSetX)
    val ypredictionTrain = predict(wPrime,bPrime,flattenedTrainingSetX)
    val trainAccuracy = 100 - abs(ypredictionTrain - trainingSetY).meanNumber().doubleValue()*100
    val testAccuracy = 100 - abs(ypredictionTest - testSetY).meanNumber().doubleValue()*100

    println(s"training accuracy:$trainAccuracy and test accuracy:$testAccuracy")
  }

}

object ImageRecognitionExercise{
  val seperator = ","


  def main(args: Array[String]): Unit = {
    val originals = loadDataSets("datasets/train_catvnoncat.h5","train_set_x","train_set_y")
    val tests = loadDataSets("datasets/test_catvnoncat.h5","test_set_x","test_set_y")
    val images = new ImageRecognitionExercise(originals._1,originals._2,tests._1,tests._2,originals._3)
    val index = 208
    val img = images.getImageNDArray(index)
    println(s"dataset shape:${images.trainingSetXOrig.shape().mkString(seperator)} and dimensions:${images.trainingSetXOrig.shape().size}")
    println(s"image shape:${img.shape().mkString(seperator)}")
    println(s"trainingSet y details ${images.trainingSetY.shape().mkString(seperator)}")
//    println(s"training set y : ${datasets.trainingSetY}")
    println(s"${images.trainingSetY(index)}")
    println(s"training set size:${images.mTrainingExamples}")
    println(s"test set size:${images.mTestExamples}")
    println(s"number of pixels per image: ${images.numberOfPixelsForImage}")
    val trainingsetFlattened = images.flattenedTrainingSetX
    images.flattenedTestSetX
    val sample = trainingsetFlattened(0->5,0)
//    println(sample)
//    println(images.standardizedX)
    images.buildAndRunModel(2000,0.005d,true)
  }

  def loadSet(reader:IHDF5Reader,name: String):INDArray= {
    val t = reader.getDataSetInformation(name)
    println(s"info dimensions: ${t.getDimensions.length}")
    //    t.getDimensions.foreach(dim =>println(dim))
    val dims = t.getDimensions.map(x => x.toInt)
    val rawSet:Array[Double] = reader.readDoubleArray(name)//train_set_x,train_set_y,test_set_x,test_set_y,list_classes
    println(rawSet.length)
    if(dims.length == 1)
      rawSet.toNDArray.reshape(1,dims(0))
    else
      rawSet.toNDArray.reshape(dims:_*)
  }

  def loadDataSets(filePath:String,xName:String,yName:String): Tuple3[INDArray,INDArray,Array[String]] ={
    val reader = HDF5Factory.openForReading(filePath)
    val setX = loadSet(reader,xName)
    val setY = loadSet(reader,yName)
    val t = reader.getDataSetInformation("list_classes")
    val dims = t.getDimensions.map(x => x.toInt)
    val labelSet:Array[String] = reader.readStringArray("list_classes")
    Tuple3[INDArray,INDArray,Array[String]](setX,setY,labelSet)
  }
  def initializeWithZeros(dim1: Int):Tuple2[INDArray,Double] = {
    initializeWithZeros(dim1,1)
  }

  def initializeWithZeros(dim1:Int,dim2:Int):Tuple2[INDArray,Double] = {
    Tuple2(Nd4j.zeros(dim1,dim2),0.0d)
  }

  private def activationFunction(wtxplusb: INDArray):INDArray = {
    sigmoid(wtxplusb)
//    tanh(wtxplusb)
//    relu(wtxplusb)
  }

  def propogate(w:INDArray, b:Double, xValues:INDArray, yValues:INDArray):GradientsAndCost = {
    val m = xValues.shape()(1).toDouble
//    println(s"shape of w.T:${w.transpose().shape().mkString(seperator)}")
//    println(s"shape of xValues:${xValues.shape().mkString(seperator)}")
    val wtxplusb = w.transpose().dot(xValues)+b
//    println(wtxplusb)
//    println(s"wtxplusb info ${wtxplusb.length()} and shape:${wtxplusb.shape().mkString(seperator)}")
    val activation = activationFunction(wtxplusb)
    val ones = Nd4j.ones(activation.shape()(0), activation.shape()(1))
    val loss = yValues*log(activation) + (ones - yValues)*log(ones - activation)
    val cost = -(1/m)* loss.sumNumber().doubleValue()
    val dw = xValues.dot((activation-yValues).transpose()).div(m)
    val db = (activation - yValues).sumNumber().doubleValue()/m
    GradientsAndCost(dw,db,cost)
  }

  def optimize(w:INDArray,b:Double,xValues:INDArray,yValues:INDArray,numIterations:Int,learningRate:Double,printCost:Boolean):Tuple3[INDArray,Double,List[Double]]={
    @tailrec
    def doOptimize(w:INDArray,b:Double,
                   iteration:Int,costs:List[Double]):Tuple3[INDArray,Double,List[Double]] ={
      if(iteration == numIterations){
        (w,b,costs)
      }else{
        val gradsAndCost = propogate(w,b,xValues,yValues)
        val dw = gradsAndCost.dw
        val db = gradsAndCost.db
        val cost = gradsAndCost.cost
        if(printCost) {
          if(iteration == 0 || iteration % 100 == 0)
            println(s"cost at iteration $iteration is $cost")
//          }
//          println(s"at iteration ${iteration+1} \n")
//          println(s"  grads and costs are $gradsAndCost")
        }
        doOptimize(w-dw*learningRate, b-db*learningRate,iteration+1,cost::costs)
      }
    }
    doOptimize(w,b,0,List())
  }

  def predict(w:INDArray,b:Double,xValues:INDArray):INDArray={
    val m = xValues.shape()(1)
    val wprime = w.reshape(xValues.shape()(0),1)
    val yPredictions = Nd4j.zeros(1,m)
    val activation = activationFunction(w.transpose().dot(xValues) + b)
    for(i <- 0 until activation.shape()(1)){
      if(activation(0,i) < 0.5)
        yPredictions(0,i) = 0.0
      else
        yPredictions(0,i) = 1.0
    }
    yPredictions
  }
}
case class GradientsAndCost(dw:INDArray,db:Double,cost:Double)
