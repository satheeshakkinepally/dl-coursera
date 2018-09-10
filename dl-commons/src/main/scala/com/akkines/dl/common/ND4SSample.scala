package com.akkines.dl.common

import org.nd4j.linalg.factory.Nd4j
import org.nd4s.Implicits._
import org.nd4j.linalg.ops.transforms.Transforms._
/**
  * Created by Satheesh Akkinepally on 9/8/18.
  */
object ND4SSample {
  def main(args: Array[String]): Unit = {
    val arr = (1 to 9).asNDArray(3,3)
    println(s"array is $arr")
    val arr2 = Nd4j.ones(4)
    println(arr2.shapeInfoToString())
    val sub = arr(0->2,1->3)
    println(sub)

    val a1 = Array(Array(1,2,3),Array(4,5,6)).toNDArray
    println(s"a1 is \n$a1")
    println(s"a1(0,0):${a1(0,1)}")
    println(s"a1(0,->):${a1(0,->)}")
    println(s"a1(1,->):${a1(1,->)}")
    println(s"a1(->,0):${a1(->,0)}")
    println(s"a1(->,1):${a1(->,1)}")
    println(s"arr is \n$arr")
    println(s"arr(0 -> 3 by 2, ->) is \n${arr(0 -> 3 by 2, ->)}")
//    val filtered = arr.filter(_ > 3)
    val f1 = arr.gte(3)
    // [[    0.7311,    0.8808,    0.9526,    0.9820,    0.9933,    0.9975],
    // [    0.9991,    0.9997,    0.9999,    1.0000,    1.0000,    1.0000]]

//    println(s"arr.filter(_ > 3) is $filtered") //todo why is not this working ?
    println(s"f1 is $f1")
    val nd = Nd4j.create(Array[Double](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), Array[Int](2, 6))
    println(s"***original nd is \n$nd")
    val ndv = sigmoid(nd,true)
    println(s"***applying sigmoid, transformed matrix is \n$ndv")
  }
}
