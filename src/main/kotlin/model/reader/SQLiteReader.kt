package model.reader

import model.graph.Graph
import model.graph.Vertex

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class SQLiteReader: Reader {

    private fun createTable(connection: Connection) {
        val statement = connection.createStatement()
        val createTableVertex = """
        CREATE TABLE IF NOT EXISTS vertex (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            vertex_key INTEGER,
            graph_id INTEGER,
            FOREIGN KEY (graph_id) REFERENCES graph(graph_id)
        )
    """
        val createTableEdge = """
        CREATE TABLE IF NOT EXISTS edge (
            start_vertex_id INTEGER,
            end_vertex_id INTEGER,
            weight INTEGER,
            FOREIGN KEY (end_vertex_id) REFERENCES graph(graph_id),
            FOREIGN KEY (start_vertex_id) REFERENCES graph(graph_id)
        )
    """
        val createTableGraph = """
        CREATE TABLE IF NOT EXISTS graph (
            graph_id INTEGER PRIMARY KEY AUTOINCREMENT,
            graph_name TEXT NOT NULL
        )
    """
        statement.execute(createTableGraph)
        statement.execute(createTableVertex)
        statement.execute(createTableEdge)
        statement.close()
    }

    private fun insertGraph(connect: Connection, graph: Graph, nameGraph: String){
        val insertName = "INSERT INTO graph (graph_name) VALUES (?)"
        val insertNameStmt: PreparedStatement = connect.prepareStatement(insertName)
        insertNameStmt.setString(1, nameGraph)
        insertNameStmt.executeUpdate()

        val graphId = insertNameStmt.generatedKeys.getInt(1)
        insertNameStmt.close()

        val insertVertexSql = "INSERT INTO vertex (vertex_key, graph_id) VALUES (?, ?)"
        val insertVertexStmt: PreparedStatement = connect.prepareStatement(insertVertexSql)

        val vertexIdMap = mutableMapOf<Vertex, Int>()

        for (vertex in graph.vertices){
            insertVertexStmt.setInt(1, vertex.key)
            insertVertexStmt.setInt(2, graphId)

            insertVertexStmt.executeUpdate()

            val vertexIdResult = insertVertexStmt.generatedKeys
            if (vertexIdResult.next()) {
                val vertexId = vertexIdResult.getInt(1)
                vertexIdMap[vertex] = vertexId
            }
        }
        insertVertexStmt.close()

        val insertEdgeSql = "INSERT INTO edge (start_vertex_id, end_vertex_id, weight) VALUES (?, ?, ?)"
        val insertEdgeStmt: PreparedStatement = connect.prepareStatement(insertEdgeSql)

        for ((vertex, edges) in graph.adjacencyList) {
            val startVertexId = vertexIdMap[vertex] ?: throw Exception("Vertex not found in vertexIdMap")
            for (edge in edges) {
                val endVertexId = vertexIdMap[edge.second] ?: throw Exception("End vertex not found in vertexIdMap")
                insertEdgeStmt.setInt(1, startVertexId)
                insertEdgeStmt.setInt(2, endVertexId)
                insertEdgeStmt.setLong(3, edge.weight)
                insertEdgeStmt.executeUpdate()
            }
        }
        insertEdgeStmt.close()
    }

    override fun saveGraph(graph: Graph, filepath: String, nameGraph: String) {
        //Сконектились с базой
        val connection = DriverManager.getConnection("jdbc:sqlite:$filepath")
        //Создали таблицы и связи между ними
        createTable(connection)
        //Сохранили граф по полочкам:)
        insertGraph(connection, graph, nameGraph)
    }

    override fun loadGraph(filepath: String, nameGraph: String): Graph {
        TODO("Load graph")
    }
}


