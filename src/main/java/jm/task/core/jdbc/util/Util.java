package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.dialect.Database;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.*;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class Util {
    private static EntityManagerFactory entityManagerFactory;

    // реализуйте настройку соеденения с БД
    public static Connection getConnection(String dbName) throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + dbName,
                "root",
                "");
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(
                    new PersistenceUnitInfoImpl(),
                    null
            );
        }

        return entityManagerFactory.createEntityManager();
    }

    // Реализация интерфейса, котороя инкапсулирует информацию о конфигурации JPA Persistence Unit
    // (здесь все по максимуму "захардкожено" для сокращения кол-ва строк)
    private static class PersistenceUnitInfoImpl implements PersistenceUnitInfo {
        public static final String JPA_VERSION = "2.1";
        private final List<String> managedClassNames;
        private final Properties properties;

        public PersistenceUnitInfoImpl() {
            managedClassNames = new ArrayList<>();
            managedClassNames.add(User.class.getName());

            properties = new Properties();
            properties.put("hibernate.dialect", Database.MYSQL.latestDialect());
            properties.put("javax.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
            properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost/coretaskdb");
            properties.put("javax.persistence.jdbc.user", "root");
            properties.put("javax.persistence.jdbc.password", "");
        }

        @Override
        public String getPersistenceUnitName() {
            return "jm.task.core.jdbc.jpa";
        }

        @Override
        public String getPersistenceProviderClassName() {
            return HibernatePersistenceProvider.class.getName();
        }

        @Override
        public PersistenceUnitTransactionType getTransactionType() {
            return PersistenceUnitTransactionType.RESOURCE_LOCAL;
        }

        @Override
        public DataSource getJtaDataSource() {
            return null;
        }

        @Override
        public DataSource getNonJtaDataSource() {
            return null;
        }

        @Override
        public List<String> getMappingFileNames() {
            return Collections.emptyList();
        }

        @Override
        public List<URL> getJarFileUrls() {
            return Collections.emptyList();
        }

        @Override
        public URL getPersistenceUnitRootUrl() {
            return null;
        }

        @Override
        public List<String> getManagedClassNames() {
            return managedClassNames;
        }

        @Override
        public boolean excludeUnlistedClasses() {
            return false;
        }

        @Override
        public SharedCacheMode getSharedCacheMode() {
            return SharedCacheMode.UNSPECIFIED;
        }

        @Override
        public ValidationMode getValidationMode() {
            return ValidationMode.AUTO;
        }

        @Override
        public Properties getProperties() {
            return properties;
        }

        @Override
        public String getPersistenceXMLSchemaVersion() {
            return JPA_VERSION;
        }

        @Override
        public ClassLoader getClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }

        @Override
        public void addTransformer(ClassTransformer classTransformer) {}

        @Override
        public ClassLoader getNewTempClassLoader() {
            return null;
        }
    }
}
