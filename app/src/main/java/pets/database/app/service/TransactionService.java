package pets.database.app.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.*;
import pets.database.app.repository.TransactionDao;
import pets.database.app.util.Util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionService {

    private static final String ERROR_RETRIEVING_TRANSACTION = "Error Retrieving Transaction, Please Try Again!!!";
    private static final String ERROR_SAVING_TRANSACTION = "Error Saving Transaction, Please Try Again!!!";
    private static final String ERROR_UPDATING_TRANSACTION = "Error Updating Transaction, Please Try Again!!!";
    private static final String ERROR_DELETING_TRANSACTION = "Error Deleting Transaction, Please Try Again!!!";

    private static Transaction convertDtoToObject(TransactionDto transactionDto) {
        return Transaction.builder()
                .id(transactionDto.getId() == null ? null : transactionDto.getId().toString())
                .description(transactionDto.getDescription())
                .account(Account.builder()
                        .id(transactionDto.getAccount().getId().toString())
                        .build())
                .trfAccount(transactionDto.getTrfAccount() == null ? null : Account.builder()
                        .id(transactionDto.getTrfAccount().getId().toString())
                        .build())
                .refTransactionType(RefTransactionType.builder()
                        .id(transactionDto.getRefTransactionType().getId().toString())
                        .build())
                .refCategory(RefCategory.builder()
                        .id(transactionDto.getRefCategory().getId().toString())
                        .build())
                .refMerchant(RefMerchant.builder()
                        .id(transactionDto.getRefMerchant().getId().toString())
                        .build())
                .user(User.builder()
                        .username(transactionDto.getUser().getUsername())
                        .build())
                .date(transactionDto.getDate())
                .amount(new BigDecimal(transactionDto.getAmount()))
                .regular(transactionDto.getRegular())
                .necessary(transactionDto.getNecessary())
                .creationDate(transactionDto.getCreationDate())
                .lastModified(transactionDto.getLastModified())
                .build();
    }

    private static TransactionDto convertObjectToDto(TransactionRequest transactionRequest) {
        return TransactionDto.builder()
                .description(transactionRequest.getDescription())
                .account(AccountDto.builder()
                        .id(new ObjectId(transactionRequest.getAccountId()))
                        .build())
                .trfAccount(!Util.hasText(transactionRequest.getTrfAccountId()) ? null : AccountDto.builder()
                        .id(new ObjectId(transactionRequest.getTrfAccountId()))
                        .build())
                .refTransactionType(RefTransactionTypeDto.builder()
                        .id(new ObjectId(transactionRequest.getTypeId()))
                        .build())
                .refCategory(RefCategoryDto.builder()
                        .id(new ObjectId(transactionRequest.getCategoryId()))
                        .build())
                .refMerchant(RefMerchantDto.builder()
                        .id(new ObjectId(transactionRequest.getMerchantId()))
                        .build())
                .user(UserDto.builder()
                        .username(transactionRequest.getUsername())
                        .build())
                .date(transactionRequest.getDate())
                .amount(transactionRequest.getAmount().toString())
                .regular(transactionRequest.getRegular())
                .necessary(transactionRequest.getNecessary())
                .build();
        // to builder trfAccount
    }

    public static TransactionResponse getAllTransactionsByUsername(String username) {
        log.info("Before Get All Transactions By Username: {}", username);
        List<Transaction> transactions = new ArrayList<>();
        Status status = null;

        try {
            List<TransactionDto> transactionDtoList = TransactionDao.getAllTransactionsByUsername(username);
            transactions = transactionDtoList.stream()
                    .map(TransactionService::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(transaction -> Util.hasText(transaction.getId()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Get All Transactions By Username: {}", username, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Transactions By Username: {}", transactions.size());
        return TransactionResponse.builder()
                .transactions(transactions)
                .status(status)
                .build();
    }

    public static TransactionResponse getTransactionById(String id) {
        log.info("Before Get Transaction By Id: {}", id);
        Transaction transaction = null;
        Status status = null;

        try {
            TransactionDto transactionDto = TransactionDao.getTransactionById(id);
            if (transactionDto != null) {
                transaction = convertDtoToObject(transactionDto);
            }
        } catch (Exception ex) {
            log.error("Get Transaction By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get Transaction By Id: {}", transaction);
        return TransactionResponse.builder()
                .transactions(transaction == null ? Collections.emptyList() : List.of(transaction))
                .status(status)
                .build();
    }

    public static TransactionResponse saveNewTransaction(TransactionRequest transactionRequest) {
        log.info("Before Save New Transaction: {}", transactionRequest);
        Transaction transaction = null;
        Status status = null;

        try {
            TransactionDto transactionDto = convertObjectToDto(transactionRequest);
            transactionDto.setCreationDate(LocalDate.now().toString());
            transactionDto.setLastModified(LocalDateTime.now().toString());

            String insertedId = TransactionDao.saveNewTransaction(transactionDto);
            if (Util.hasText(insertedId)) {
                transaction = convertDtoToObject(transactionDto);
                transaction.setId(insertedId);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_SAVING_TRANSACTION)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Save New Transaction: {}", transactionRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_SAVING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Save New Transaction: {}", transaction);
        return TransactionResponse.builder()
                .transactions(transaction == null ? Collections.emptyList() : List.of(transaction))
                .status(status)
                .build();
    }

    public static TransactionResponse updateTransactionById(String id, TransactionRequest transactionRequest) {
        log.info("Before Update Transaction By Id: {} | {}", id, transactionRequest);
        TransactionResponse transactionResponse;
        Status status;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            List<Bson> bsonList = new ArrayList<>();

            if (Util.hasText(transactionRequest.getDescription())) {
                bsonList.add(Updates.set("description", transactionRequest.getDescription()));
            }

            if (Util.hasText(transactionRequest.getAccountId())) {
                bsonList.add(Updates.set("account._id", new ObjectId(transactionRequest.getAccountId())));
            }

            if (Util.hasText(transactionRequest.getTrfAccountId())) {
                bsonList.add(Updates.set("trfAccount._id", new ObjectId(transactionRequest.getTrfAccountId())));
            }

            if (Util.hasText(transactionRequest.getTypeId())) {
                bsonList.add(Updates.set("refTransactionType._id", new ObjectId(transactionRequest.getTypeId())));
            }

            if (Util.hasText(transactionRequest.getCategoryId())) {
                bsonList.add(Updates.set("refCategory._id", new ObjectId(transactionRequest.getCategoryId())));
            }

            if (Util.hasText(transactionRequest.getMerchantId())) {
                bsonList.add(Updates.set("refMerchant._id", new ObjectId(transactionRequest.getMerchantId())));
            }

            if (Util.hasText(transactionRequest.getDate())) {
                bsonList.add(Updates.set("date", transactionRequest.getDate()));
            }

            bsonList.add(Updates.set("amount", transactionRequest.getAmount().toString()));
            bsonList.add(Updates.set("regular", transactionRequest.getRegular()));
            bsonList.add(Updates.set("necessary", transactionRequest.getNecessary()));
            bsonList.add(Updates.set("lastModified", LocalDateTime.now().toString()));

            Bson updates = Updates.combine(bsonList);
            long modifiedCount = TransactionDao.updateTransactionById(filter, updates);

            if (modifiedCount > 0) {
                transactionResponse = getTransactionById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_TRANSACTION)
                        .build();
                transactionResponse = TransactionResponse.builder()
                        .transactions(Collections.emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Update Transaction By Id: {} | {}", id, transactionRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_TRANSACTION)
                    .message(ex.toString())
                    .build();
            transactionResponse = TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(status)
                    .build();
        }

        log.info("After Update Transaction By Id: {} | user: {}", id, transactionResponse);
        return transactionResponse;
    }

    public static TransactionResponse deleteTransactionById(String id) {
        log.info("Before Delete Transaction By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            deleteCount = TransactionDao.deleteTransactionById(filter);
        } catch (Exception ex) {
            log.error("Delete Transaction By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete Transaction By Id: {} | deleteCount: {}", id, deleteCount);
        return TransactionResponse.builder()
                .transactions(Collections.emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }

    public static TransactionResponse deleteTransactionByAccountId(String id) {
        log.info("Before Delete Transaction By Account Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("account._id", new ObjectId(id));
            deleteCount = TransactionDao.deleteTransactionByAccountId(filter);
        } catch (Exception ex) {
            log.error("Delete Transaction By Account Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete Transaction By Account Id: {} | deleteCount: {}", id, deleteCount);
        return TransactionResponse.builder()
                .transactions(Collections.emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
