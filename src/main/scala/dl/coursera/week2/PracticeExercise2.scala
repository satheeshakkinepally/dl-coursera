package dl.coursera.week2

import dl.coursera.week2.ImageRecognitionExercise._
import org.nd4j.linalg.ops.transforms.Transforms.sigmoid
import org.nd4s.Implicits._

/**
  * Created by Satheesh Akkinepally on 9/13/18.
  */
object PracticeExercise2 {
  def main(args: Array[String]): Unit = {
    println(s"sigmoid([0,2]) = ${sigmoid(Array(0,2).toNDArray)}")
    val twoDimZeroMatrix = initializeWithZeros(2)
    println(s"zero initialized for 2 dim = $twoDimZeroMatrix")
    println(s"shape of twoDimZeroMatrix = ${twoDimZeroMatrix._1.shape().mkString(seperator)} and b = ${twoDimZeroMatrix._2}")
    //test cost functions
    val w = Array(Array(1.0),Array(2.0)).toNDArray
    val b = 2.0
    val X = Array(Array(1.0,2.0,-1.0),Array(3.0,4.0,-3.2)).toNDArray
    val Y = Array(1,0,1).toNDArray
    val gradsAndCost = propogate(w,b,X,Y)
    println(s"gradsAndCost = $gradsAndCost")
    val optimized = optimize(w,b,X,Y,100,0.009,printCost = true)
    println(s"optimized params:\n${optimized._1} and b: ${optimized._2}")

    val wPrime = Array(Array(0.1124579),Array(0.23106775)).toNDArray
    val bprime = -0.3
    val Xprime = Array(Array(1.0,-1.1,-3.2),Array(1.2,2.0,0.1)).toNDArray
    val predictions = predict(wPrime,bprime,Xprime)
    println(predictions)

    val testArray = Array(Array(-6.415e4d,-1.0473e5d,-1.2214e5d)).toNDArray
    val sigmoidV = sigmoid(testArray)
    println(s"sigmoidV:$sigmoidV")
    val dVal = -6.415e4

  }
}
