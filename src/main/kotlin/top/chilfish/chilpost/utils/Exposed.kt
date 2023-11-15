package top.chilfish.chilpost.utils

import org.jetbrains.exposed.sql.FieldSet
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.TransactionManager

/**
 * Execute a native SQL query and return a list of [ResultRow]
 */
fun FieldSet.query(query: String): List<ResultRow> {
    val fieldsIndex = realFields.toSet().mapIndexed { index, expression -> expression to index }.toMap()
    val resultRows = mutableListOf<ResultRow>()
    TransactionManager.current().exec(query) { resultSet ->
        while (resultSet.next()) {
            resultRows.add(ResultRow.create(resultSet, fieldsIndex))
        }
    }
    return resultRows
}
