package pets.database.app.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import pets.database.app.model.*;
import pets.database.app.util.MongoDbUtil;

import java.util.ArrayList;
import java.util.List;

public class RefTypesDao {

    private static final String REF_ACCOUNT_TYPE_DETAILS = "ref_account_type_details";
    private static final String REF_BANK_DETAILS = "ref_bank_details";
    private static final String REF_CATEGORY_TYPE_DETAILS = "ref_category_type_details";
    private static final String REF_CATEGORY_DETAILS = "ref_category_details";
    private static final String REF_TRANSACTION_TYPE_DETAILS = "ref_transaction_type_details";

    public List<RefAccountTypeDto> getAllRefAccountTypes() {
        List<RefAccountTypeDto> accountTypeDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            MongoCollection<RefAccountTypeDto> mongoCollection = (MongoCollection<RefAccountTypeDto>)
                    MongoDbUtil.getMongoCollection(mongoClient, REF_ACCOUNT_TYPE_DETAILS, RefAccountTypeDto.class);
            FindIterable<RefAccountTypeDto> findIterable = mongoCollection.find();
            findIterable.forEach(accountTypeDtoList::add);
        }
        return accountTypeDtoList;
    }

    public List<RefBankDto> getAllRefBanks() {
        List<RefBankDto> refBankDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            MongoCollection<RefBankDto> mongoCollection = (MongoCollection<RefBankDto>)
                    MongoDbUtil.getMongoCollection(mongoClient, REF_BANK_DETAILS, RefBankDto.class);
            FindIterable<RefBankDto> findIterable = mongoCollection.find();
            findIterable.forEach(refBankDtoList::add);
        }
        return refBankDtoList;
    }

    public List<RefCategoryTypeDto> getAllRefCategoryTypes() {
        List<RefCategoryTypeDto> refCategoryTypeDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            MongoCollection<RefCategoryTypeDto> mongoCollection = (MongoCollection<RefCategoryTypeDto>)
                    MongoDbUtil.getMongoCollection(mongoClient, REF_CATEGORY_TYPE_DETAILS, RefCategoryTypeDto.class);
            FindIterable<RefCategoryTypeDto> findIterable = mongoCollection.find();
            findIterable.forEach(refCategoryTypeDtoList::add);
        }
        return refCategoryTypeDtoList;
    }

    public List<RefCategoryDto> getAllRefCategories() {
        List<RefCategoryDto> refCategoryDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            MongoCollection<RefCategoryDto> mongoCollection = (MongoCollection<RefCategoryDto>)
                    MongoDbUtil.getMongoCollection(mongoClient, REF_CATEGORY_DETAILS, RefCategoryDto.class);
            FindIterable<RefCategoryDto> findIterable = mongoCollection.find();
            findIterable.forEach(refCategoryDtoList::add);
        }
        return refCategoryDtoList;
    }

    public List<RefTransactionTypeDto> getAllRefTransactionTypes() {
        List<RefTransactionTypeDto> refTransactionTypeDtoList = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(MongoDbUtil.getMongoClientSettings())) {
            MongoCollection<RefTransactionTypeDto> mongoCollection = (MongoCollection<RefTransactionTypeDto>)
                    MongoDbUtil.getMongoCollection(mongoClient, REF_TRANSACTION_TYPE_DETAILS, RefTransactionTypeDto.class);
            FindIterable<RefTransactionTypeDto> findIterable = mongoCollection.find();
            findIterable.forEach(refTransactionTypeDtoList::add);
        }
        return refTransactionTypeDtoList;
    }
}
