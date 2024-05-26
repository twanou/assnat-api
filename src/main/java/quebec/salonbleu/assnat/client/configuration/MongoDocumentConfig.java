package quebec.salonbleu.assnat.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import quebec.salonbleu.assnat.client.documents.UuidDocument;

import java.util.UUID;

@Configuration
public class MongoDocumentConfig {

    // Pour ne pas avoir de _class dans la BD.
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingConverter.setCustomConversions(MongoCustomConversions.create(MongoCustomConversions.MongoConverterConfigurationAdapter::useNativeDriverJavaTimeCodecs));
        return mappingConverter;
    }

    @Bean
    public BeforeConvertCallback<UuidDocument> beforeSaveCallback() {
        return (entity, collection) -> entity.getId() == null ? entity.withId(UUID.randomUUID()) : entity;
    }
}
