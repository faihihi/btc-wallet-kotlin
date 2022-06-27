package com.btc.wallet.db

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration
@EnableCassandraRepositories(basePackages = ["com.btc.wallet.db"])
class DBConfig(
    @Value("\${spring.data.cassandra.keyspace-name}") val _keyspaceName: String
) : AbstractCassandraConfiguration() {

    override fun getKeyspaceName(): String = _keyspaceName

    override fun getKeyspaceCreations() =
        listOf(CreateKeyspaceSpecification.createKeyspace(keyspaceName).withSimpleReplication().ifNotExists())

    override fun getSchemaAction(): SchemaAction = SchemaAction.CREATE_IF_NOT_EXISTS

    override fun getEntityBasePackages(): Array<String> = arrayOf("com.btc.wallet.db")
}