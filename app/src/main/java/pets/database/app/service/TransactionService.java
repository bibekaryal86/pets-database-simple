package pets.database.app.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.Account;
import pets.database.app.model.AccountDto;
import pets.database.app.model.RefCategory;
import pets.database.app.model.RefCategoryDto;
import pets.database.app.model.RefMerchant;
import pets.database.app.model.RefMerchantDto;
import pets.database.app.model.RefTransactionType;
import pets.database.app.model.RefTransactionTypeDto;
import pets.database.app.model.Status;
import pets.database.app.model.Transaction;
import pets.database.app.model.TransactionDto;
import pets.database.app.model.TransactionRequest;
import pets.database.app.model.TransactionResponse;
import pets.database.app.model.User;
import pets.database.app.model.UserDto;
import pets.database.app.repository.TransactionDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static pets.database.app.util.Util.hasText;

@Slf4j
public class TransactionService {

    private static final String ERROR_RETRIEVING_TRANSACTION = "Error Retrieving Transaction, Please Try Again!!!";
    private static final String ERROR_SAVING_TRANSACTION = "Error Saving Transaction, Please Try Again!!!";
    private static final String ERROR_UPDATING_TRANSACTION = "Error Updating Transaction, Please Try Again!!!";
    private static final String ERROR_DELETING_TRANSACTION = "Error Deleting Transaction, Please Try Again!!!";

    private Transaction convertDtoToObject(TransactionDto transactionDto) {
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

    private TransactionDto convertObjectToDto(TransactionRequest transactionRequest) {
        return TransactionDto.builder()
                .description(transactionRequest.getDescription())
                .account(AccountDto.builder()
                        .id(new ObjectId(transactionRequest.getAccountId()))
                        .build())
                .trfAccount(!hasText(transactionRequest.getTrfAccountId()) ? null : AccountDto.builder()
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
    }

    public TransactionResponse getAllTransactionsByUsername(String username) {
        log.info("Before Get All Transactions By Username: {}", username);
        List<Transaction> transactions = new ArrayList<>();
        Status status = null;

        try {
            List<TransactionDto> transactionDtoList = new TransactionDao().getAllTransactionsByUsername(username);
            transactions = transactionDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(transaction -> hasText(transaction.getId()))
                    .collect(toList());
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

    public TransactionResponse getTransactionById(String id) {
        log.info("Before Get Transaction By Id: {}", id);
        Transaction transaction = null;
        Status status = null;

        try {
            TransactionDto transactionDto = new TransactionDao().getTransactionById(id);
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
                .transactions(transaction == null ? emptyList() : List.of(transaction))
                .status(status)
                .build();
    }

    public TransactionResponse saveNewTransaction(TransactionRequest transactionRequest) {
        log.info("Before Save New Transaction: {}", transactionRequest);
        Transaction transaction = null;
        Status status = null;

        try {
            TransactionDto transactionDto = convertObjectToDto(transactionRequest);
            transactionDto.setCreationDate(LocalDate.now().toString());
            transactionDto.setLastModified(LocalDateTime.now().toString());

            String insertedId = new TransactionDao().saveNewTransaction(transactionDto);
            if (hasText(insertedId)) {
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
                .transactions(transaction == null ? emptyList() : List.of(transaction))
                .status(status)
                .build();
    }

    public TransactionResponse updateTransactionById(String id, TransactionRequest transactionRequest) {
        log.info("Before Update Transaction By Id: {} | {}", id, transactionRequest);
        TransactionResponse transactionResponse;
        Status status;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            List<Bson> bsonList = new ArrayList<>();

            if (hasText(transactionRequest.getDescription())) {
                bsonList.add(Updates.set("description", transactionRequest.getDescription()));
            }

            if (hasText(transactionRequest.getAccountId())) {
                bsonList.add(Updates.set("account._id", new ObjectId(transactionRequest.getAccountId())));
            }

            if (hasText(transactionRequest.getTrfAccountId())) {
                bsonList.add(Updates.set("trfAccount._id", new ObjectId(transactionRequest.getTrfAccountId())));
            }

            if (hasText(transactionRequest.getTypeId())) {
                bsonList.add(Updates.set("refTransactionType._id", new ObjectId(transactionRequest.getTypeId())));
            }

            if (hasText(transactionRequest.getCategoryId())) {
                bsonList.add(Updates.set("refCategory._id", new ObjectId(transactionRequest.getCategoryId())));
            }

            if (hasText(transactionRequest.getMerchantId())) {
                bsonList.add(Updates.set("refMerchant._id", new ObjectId(transactionRequest.getMerchantId())));
            }

            if (hasText(transactionRequest.getDate())) {
                bsonList.add(Updates.set("date", transactionRequest.getDate()));
            }

            bsonList.add(Updates.set("amount", transactionRequest.getAmount().toString()));
            bsonList.add(Updates.set("regular", transactionRequest.getRegular()));
            bsonList.add(Updates.set("necessary", transactionRequest.getNecessary()));
            bsonList.add(Updates.set("lastModified", LocalDateTime.now().toString()));

            Bson updates = Updates.combine(bsonList);
            long modifiedCount = new TransactionDao().updateTransactionById(filter, updates);

            if (modifiedCount > 0) {
                transactionResponse = getTransactionById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_TRANSACTION)
                        .build();
                transactionResponse = TransactionResponse.builder()
                        .transactions(emptyList())
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
                    .transactions(emptyList())
                    .status(status)
                    .build();
        }

        log.info("After Update Transaction By Id: {} | user: {}", id, transactionResponse);
        return transactionResponse;
    }

    public TransactionResponse deleteTransactionById(String id) {
        log.info("Before Delete Transaction By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            deleteCount = new TransactionDao().deleteTransactionById(filter);
        } catch (Exception ex) {
            log.error("Delete Transaction By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete Transaction By Id: {} | deleteCount: {}", id, deleteCount);
        return TransactionResponse.builder()
                .transactions(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }

    public TransactionResponse deleteTransactionByAccountId(String id) {
        log.info("Before Delete Transaction By Account Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("account._id", new ObjectId(id));
            deleteCount = new TransactionDao().deleteTransactionByAccountId(filter);
        } catch (Exception ex) {
            log.error("Delete Transaction By Account Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_TRANSACTION)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete Transaction By Account Id: {} | deleteCount: {}", id, deleteCount);
        return TransactionResponse.builder()
                .transactions(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
