package com.akkines.dl.common

import org.nd4j.linalg.factory.Nd4j
import org.nd4s.Implicits._

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
    println(s"arr.filter(_ > 3) is ${arr.filter(_ > 3)}")
  }
}
