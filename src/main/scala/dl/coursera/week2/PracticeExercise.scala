package dl.coursera.week2

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4s.Implicits._
import org.nd4j.linalg.ops.transforms.Transforms._

/**
  * Created by Satheesh Akkinepally on 9/09/18.
  */
object PracticeExercise {

  private def testBasicSigmoid() = {
    var x = Array(1, 2, 3).toNDArray
    var y = basicSigmoid(x)
    println(y)
  }

  private def testBasicSigmoidDerivative() = {
    var x = Array(1, 2, 3).toNDArray
    var y = basicExp(x)
    println(y)
    y = sigmoidDerivative(x)
    println(s"sigmoid derivatives: $y")
  }

  private def testImageToVector() = {
    val img = Nd4j.ones(3, 3, 2)
    println(s"img is $img")
    println(s"imgToVec ${imageToVector(img)}")
    val arr1 = Array(Array(0.67826139, 0.29380381),
      Array(0.90714982, 0.52835647),
      Array(0.4215251, 0.45017551)).toNDArray
    val arr2 = Array(Array(0.92814219, 0.96677647),
      Array(0.85304703, 0.52351845),
      Array(0.19981397, 0.27417313)).toNDArray
    val arr3 = Array(Array(0.60659855, 0.00533165),
      Array(0.10820313, 0.49978937),
      Array(0.34144279, 0.94630077)).toNDArray

    val imag1 = Nd4j.toFlattened(arr1, arr2, arr3).reshape(3, 3, 2)
    println(imag1)
    println(s"imgToVec ${imageToVector(imag1)}")
  }

  def testNormalize() = {
    val toNormalize = Array(Array(0.0, 3.0, 4.0), Array(1.0, 6.0, 4.0)).toNDArray
    val normalized = normalize(toNormalize)
    println(s"toNormalize:\n$toNormalize")
    println(s"normalized:\n$normalized")
  }

  def testSoftmax() = {
    val toSoftmax = Array(Array(9.0, 2.0, 5.0, 0.0, 0.0), Array(7, 5, 0.0, 0.0, 0.0)).toNDArray
    val softmaxed = softmax(toSoftmax)
    println(s"toSoftmax:\n$toSoftmax")
    println(s"softmaxed:\n$softmaxed")
  }

  def testVectorize1() = {
    val x1 = Array[Double](9, 2, 5, 0, 0, 7, 5, 0, 0, 0, 9, 2, 5, 0, 0)
    val x2 = Array[Double](9, 2, 2, 9, 0, 9, 2, 5, 0, 0, 9, 2, 5, 0, 0)
    var start = System.currentTimeMillis()
    val x1dotx2 = classicDotProductNonVectorized1D(x1, x2)
    var end = System.currentTimeMillis()
    println(x1dotx2 + "\n" + s" time taken for classicDotProduct:${end - start} ms")
    val x1nd = x1.toNDArray
    val x2nd = x2.toNDArray.transpose()
    start = System.currentTimeMillis()
    val z = x1nd.dot(x2nd)
    end = System.currentTimeMillis()
    println(z + "\n" + s" time taken for vector DotProduct:${end - start} ms")

  }

  def testVectorize2() = {
    val m1 = (1 to 1000000).toArray[Int].map(_.toDouble)
    val m2 = (1 to 1000000).toArray[Int].map(_.toDouble)
    var start = System.currentTimeMillis()
    val m = m1.toNDArray.dot(m2.toNDArray.transpose())
    var end = System.currentTimeMillis()
    println(m + "\n" + s" time taken for vector DotProduct:${end - start} ms")
    start = System.currentTimeMillis()
    val m1dotm2 = classicDotProductNonVectorized1D(m1, m2)
    end = System.currentTimeMillis()
    println(m1dotm2 + "\n" + s" time taken for classicDotProduct:${end - start} ms")
  }

  def testLogisticRegressionLossFunctions(): Unit = {
    val yhat = Array(0.9,0.2,0.1,0.4,0.9).toNDArray
    val y = Array(1,0,0,1,1).toNDArray
    val l1Loss = logisticRegressionL1Loss(yhat,y)
    println(s"l1loss: $l1Loss")
    val l2Loss = logisticRegressionL2Loss(yhat,y)
    println(s"l2loss: $l2Loss")
  }

  def main(args: Array[String]): Unit = {
    testBasicSigmoid()
    testBasicSigmoidDerivative()
    testImageToVector()
    testNormalize()
    testSoftmax()
    testVectorize1()
    testVectorize2()
    testLogisticRegressionLossFunctions()
  }

  def basicSigmoid(matrix: INDArray): INDArray = {
    val ndv = sigmoid(matrix)
    ndv
  }

  def basicExp(matrix: INDArray): INDArray = {
    val ndv = exp(matrix)
    ndv
  }

  def sigmoidDerivative(matrix: INDArray): INDArray = {
    val sig = sigmoid(matrix)
    val ones = Nd4j.ones(sig.shape()(0), sig.shape()(1))
    val temp = ones - sig
    sig * temp
  }

  def imageToVector(image: INDArray): INDArray = {
    assert(image.shape().length >= 3)
    val length = image.shape()(0)
    val height = image.shape()(1)
    val depth = image.shape()(2)

    image.reshape(length * height * depth, 1)
  }

  def normalize(matrix: INDArray): INDArray = {
    var norm2 = matrix.norm2(1)
    val normalized = matrix.divColumnVector(norm2) //todo a simple div('/' or 'div') by the other vector does not seem to be broadcasting. Not sure why the literal 'divByColummVector' needs to be specified here
    normalized
  }

  def softmax(matrix: INDArray): INDArray = {
    var expo = exp(matrix)
    val sumColumnVector = expo.sum(1)
    val softmaxed = expo.divColumnVector(sumColumnVector)
    softmaxed
  }

  def classicDotProductNonVectorized1D(array1: Array[Double], array2: Array[Double]): Double = {
    array1.indices.toArray.map(idx => array1(idx) * array2(idx)).sum
  }

  def logisticRegressionL1Loss(yhat: INDArray, y: INDArray):Number= {
    abs(y - yhat).sumNumber()
  }

  def logisticRegressionL2Loss(yhat: INDArray, y: INDArray):Number= {
    val diff = yhat-y
    diff.dot(diff.T).sumNumber()
  }
}
