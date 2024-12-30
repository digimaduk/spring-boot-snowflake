package org.digimad.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.snowflake.client.jdbc.SnowflakeBasicDataSource;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.FileReader;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Security;

@Configuration
@Slf4j
public class SnowflakeDBConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${snowflake.key.file.path}")
    private String filepath;
    @Value("${snowflake.key.passphrase}")
    private String passphrase;
    @Value("${spring.datasource.hikari.minimumIdle}")
    private int hikariMinimumIdle;
    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private int hikariMaximumPoolSize;
    @Value("${spring.datasource.hikari.poolName}")
    private String hikariPoolName;

    @Bean(name = "snowflakeDS")
    public DataSource dataSource() throws Exception {
        PrivateKey privateKey = getPrivateKey(filepath, passphrase);
        SnowflakeBasicDataSource ds = new SnowflakeBasicDataSource();
        ds.setUrl(url);
        ds.setUser(username);
        ds.setPrivateKey(privateKey); //password
//        Properties properties = new Properties();
//        properties.put("jdbcUrl", url);
//        HikariConfig hikariConfig = new HikariConfig(properties);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(ds);
        hikariConfig.setMinimumIdle(hikariMinimumIdle);
        hikariConfig.setMaximumPoolSize(hikariMaximumPoolSize);
        hikariConfig.setPoolName(hikariPoolName);
        return new HikariDataSource(hikariConfig);
    }

    public static PrivateKey getPrivateKey(String filepath, String passphrase)
        throws Exception {
        PrivateKeyInfo privateKeyInfo = null;
        Security.addProvider(new BouncyCastleProvider());
        // Read an object from the private key file.
        PEMParser pemParser = new PEMParser(new FileReader(Paths.get(filepath).toFile()));
        Object pemObject = pemParser.readObject();
        if (pemObject instanceof PKCS8EncryptedPrivateKeyInfo) {
            // Handle the case where the private key is encrypted.
            PKCS8EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = (PKCS8EncryptedPrivateKeyInfo) pemObject;
            InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder().build(passphrase.toCharArray());
            privateKeyInfo = encryptedPrivateKeyInfo.decryptPrivateKeyInfo(pkcs8Prov);
        } else if (pemObject instanceof PrivateKeyInfo) {
            // Handle the case where the private key is unencrypted.
            privateKeyInfo = (PrivateKeyInfo) pemObject;
        }
        pemParser.close();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
        return converter.getPrivateKey(privateKeyInfo);
    }
}

