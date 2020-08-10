package processor

import java.util.*

val scanner = Scanner(System.`in`)

class DimensionException(message: String) : Exception(message)
class Matrix(val matrix : Array<DoubleArray>){
    val rowSize = matrix.size
    val colSize = matrix[0].size
}
fun deleteRow(a: Matrix, row: Int): Matrix {
    if (a.rowSize < row) {
        throw DimensionException("we don't have $row row because matrix has ${a.rowSize}rows")
    } else {
        return Matrix(a.matrix.filterIndexed { index, _ -> index != row - 1 }.toTypedArray())
    }
}

fun deleteCol(a: Matrix, col: Int): Matrix {
    if (a.colSize < col) {
        throw DimensionException("we don't have $col row because matrix has ${a.colSize}rows")
    } else {

        return Matrix(a.matrix.map { it.filterIndexed { index, _ -> index != col - 1 }.toDoubleArray() }.toTypedArray())
    }
}

fun makeNewMatrixWithRowAndColByScanner(position: String): Matrix {
    val index = when (position) {
        "1" -> "first"
        "2" -> "second"
        else -> "new"
    }
    println("Enter size of $index matrix: ")
    val (row, col) = scanner.nextLine().split(" ").run {
        Pair<Int, Int>(get(0).toInt(), get(1).toInt())
    }
    println("Enter $index matrix:")
    return Matrix(Array(row) {
        scanner.nextLine().trim().split(" ").map { it.toDouble() }.toDoubleArray().run {
            if (size != col) {
                throw DimensionException("element amount and column size dont match")
            } else {
                this
            }
        }
    })
}

fun addMatrices(a: Matrix, b: Matrix):Matrix {
    val temp1 = a.matrix
    val temp2 = b.matrix
    if (a.rowSize != b.rowSize || a.colSize!=b.colSize) {
        throw DimensionException("two matrix dimension is wrong")
    } else {
        return Matrix(temp1.mapIndexed { rowindex, row ->
            row.mapIndexed { colindex, element ->
                element + temp2[rowindex][colindex]
            }.toDoubleArray()
        }.toTypedArray())
    }
}
fun multiMatrices(a:Matrix, b: Matrix): Matrix{
    val tempA = a.matrix
    val tempB = b.matrix
    if (a.colSize!=b.rowSize) {
        throw DimensionException("two matrix dimension is wrong")
    }
    else {
        return Matrix(Array(a.rowSize) {
            rowindex ->
            Array(b.colSize){
                colindex->
                var sum = 0.0
                for(i in 0 until a.colSize){
                    sum+=tempA[rowindex][i]*tempB[i][colindex]
                }
                sum
            }.toDoubleArray()
        })

    }
}

fun multiMatrixToCon(a: Matrix, b: Double): Matrix {
    return Matrix(a.matrix.map { row ->
        row.map { element ->
            element * b
        }.toDoubleArray()
    }.toTypedArray())
}

fun InverseMatrix(a: Matrix): Matrix {
    return multiMatrixToCon(transposeMatrixMainDiagonal(Matrix(a.matrix.mapIndexed { rowIndex, doubles ->
        doubles.mapIndexed { colIndex, d ->
            if ((rowIndex + 1 + colIndex + 1) % 2 == 0) {
                1 *  determinantOfMatrix(deleteCol(deleteRow(a, rowIndex + 1), colIndex + 1))
            } else {
                -1 *  determinantOfMatrix(deleteCol(deleteRow(a, rowIndex + 1), colIndex + 1))
            }
        }.toDoubleArray()
    }.toTypedArray()), 1), 1.0 / determinantOfMatrix(a))
}

fun determinantOfMatrix(a: Matrix): Double {
    when {
        a.colSize != a.rowSize -> {
            throw DimensionException("only squares have determinant")
        }
        a.colSize == 1 -> {
            return a.matrix[0][0]
        }
        a.colSize == 2 -> {
            return a.matrix[0][0] * a.matrix[1][1] - a.matrix[1][0] * a.matrix[0][1]
        }
        else -> {
            var sum = 0.0
            for (col in 1..a.matrix[0].size){
                if (col%2==0){sum-= a.matrix[0][col-1]*determinantOfMatrix(deleteRow(deleteCol(a,col),1))}
                else {sum+= a.matrix[0][col-1]*determinantOfMatrix(deleteRow(deleteCol(a,col),1))}
            }
            return sum
        }
    }
}

fun printMatrix(matrix: Matrix) {
    for (row in matrix.matrix) {
        for (element in row) {
            print("$element ")
        }
        println()
    }
}

fun transposeMatrixMainDiagonal(matrix: Matrix, i: Int): Matrix {
    when (i) {
        1 -> {//main
            return Matrix(Array<DoubleArray>(matrix.colSize) { lowindex ->
                DoubleArray(matrix.rowSize) {
                    matrix.matrix[it][lowindex]
                }
            })
        }
        2 -> {//side
            return Matrix(Array<DoubleArray>(matrix.colSize) { lowindex ->
                DoubleArray(matrix.rowSize) {
                    matrix.matrix[matrix.rowSize - 1 - it][matrix.colSize - 1 - lowindex]
                }
            })
        }
        3 -> {//vertical
            return Matrix(Array<DoubleArray>(matrix.rowSize) { lowindex ->
                DoubleArray(matrix.colSize) {
                    matrix.matrix[lowindex][matrix.colSize-1-it]
                }
            })
        }
        else -> {//horizontal
            return Matrix(Array<DoubleArray>(matrix.rowSize) { lowindex ->
                DoubleArray(matrix.colSize) {
                    matrix.matrix[matrix.rowSize-1-lowindex][it]
                }
            })
        }
    }
}

fun askChoice() {
    println("""1. Add matrices
2. Multiply matrix to a constant
3. Multiply matrices
4. Transpose matrix
5. Calculate a determinant
6. Inverse matrix
0. Exit""")
    println("Your choice:")
}
fun askTransType(){
    println("""1. Main diagonal
2. Side diagonal
3. Vertical line
4. Horizontal line
Your choice: """)
}
fun main() {
    askChoice()
    var choice = scanner.nextLine().toInt()
    while (choice!=0){
        try{
        when(choice){
            1 ->  {
                val a = makeNewMatrixWithRowAndColByScanner("1")
                val b = makeNewMatrixWithRowAndColByScanner("2")
                val result = addMatrices(a,b)
                printMatrix(result)
            }
            2 ->  {
                val a = makeNewMatrixWithRowAndColByScanner("0")
                val con= scanner.nextLine().trim().toDouble()
                val result = multiMatrixToCon(a,con)
                printMatrix(result)
            }
            3 -> {
                val a = makeNewMatrixWithRowAndColByScanner("1")
                val b = makeNewMatrixWithRowAndColByScanner("2")
                val result = multiMatrices(a,b)
                printMatrix(result)
            }
            4 -> {
                askTransType()
                val type = scanner.nextLine().toInt()
                val a = makeNewMatrixWithRowAndColByScanner("0")
                val result = transposeMatrixMainDiagonal(a, type)
                println("The result is:")
                printMatrix(result)
            }
            5->{
                val a = makeNewMatrixWithRowAndColByScanner("0")
                println("The result is:")
                println(determinantOfMatrix(a))
            }
            6->{
                val a = makeNewMatrixWithRowAndColByScanner("0")
                println("The result is:")
                printMatrix(InverseMatrix(a))
            }

        }}
        catch (e: Exception) {
            println("ERROR")
        }
        askChoice()
        choice = scanner.nextLine().toInt()
    }

}
