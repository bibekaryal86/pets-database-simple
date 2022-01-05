package pets.database.app.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.*;
import pets.database.app.repository.AccountDao;
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
public class AccountService {

    private static final String ERROR_RETRIEVING_ACCOUNT = "Error Retrieving Account, Please Try Again!!!";
    private static final String ERROR_SAVING_ACCOUNT = "Error Saving Account, Please Try Again!!!";
    private static final String ERROR_UPDATING_ACCOUNT = "Error Updating Account, Please Try Again!!!";
    private static final String ERROR_DELETING_ACCOUNT = "Error Deleting Account, Please Try Again!!!";

    private Account convertDtoToObject(AccountDto accountDto) {
        return Account.builder()
                .id(accountDto.getId() == null ? null : accountDto.getId().toString())
                .refAccountType(RefAccountType.builder()
                        .id(accountDto.getRefAccountType().getId().toString())
                        .build())
                .refBank(RefBank.builder()
                        .id(accountDto.getRefBank().getId().toString())
                        .build())
                .description(accountDto.getDescription())
                .user(User.builder()
                        .username(accountDto.getUser().getUsername())
                        .build())
                .openingBalance(new BigDecimal(accountDto.getOpeningBalance()))
                .status(accountDto.getStatus())
                .creationDate(accountDto.getCreationDate())
                .lastModified(accountDto.getLastModified())
                .build();
    }

    private AccountDto convertObjectToDto(AccountRequest accountRequest) {
        return AccountDto.builder()
                .refAccountType(RefAccountTypeDto.builder()
                        .id(new ObjectId(accountRequest.getTypeId()))
                        .build())
                .refBank(RefBankDto.builder()
                        .id(new ObjectId(accountRequest.getBankId()))
                        .build())
                .description(accountRequest.getDescription())
                .user(UserDto.builder()
                        .username(accountRequest.getUsername())
                        .build())
                .openingBalance(accountRequest.getOpeningBalance().toString())
                .status(accountRequest.getStatus())
                .build();
    }

    public AccountResponse getAllAccountsByUsername(String username) {
        log.info("Before Get All Accounts By Username: {}", username);
        List<Account> accounts = new ArrayList<>();
        Status status = null;

        try {
            List<AccountDto> accountDtoList = new AccountDao().getAllAccountsByUsername(username);
            accounts = accountDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(account -> Util.hasText(account.getId()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Get All Accounts By Username: {}", username, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_ACCOUNT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Accounts By Username: {}", accounts.size());
        return AccountResponse.builder()
                .accounts(accounts)
                .status(status)
                .build();
    }

    public AccountResponse getAccountById(String id) {
        log.info("Before Get Account By Id: {}", id);
        Account account = null;
        Status status = null;

        try {
            AccountDto accountDto = new AccountDao().getAccountById(id);
            if (accountDto != null) {
                account = convertDtoToObject(accountDto);
            }
        } catch (Exception ex) {
            log.error("Get Account By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_ACCOUNT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get Account By Id: {}", account);
        return AccountResponse.builder()
                .accounts(account == null ? Collections.emptyList() : List.of(account))
                .status(status)
                .build();
    }

    public AccountResponse saveNewAccount(AccountRequest accountRequest) {
        log.info("Before Save New Account: {}", accountRequest);
        Account account = null;
        Status status = null;

        try {
            AccountDto accountDto = convertObjectToDto(accountRequest);
            accountDto.setCreationDate(LocalDate.now().toString());
            accountDto.setLastModified(LocalDateTime.now().toString());

            String insertedId = new AccountDao().saveNewAccount(accountDto);
            if (Util.hasText(insertedId)) {
                account = convertDtoToObject(accountDto);
                account.setId(insertedId);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_SAVING_ACCOUNT)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Save New Account: {}", accountRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_SAVING_ACCOUNT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Save New Account: {}", account);
        return AccountResponse.builder()
                .accounts(account == null ? Collections.emptyList() : List.of(account))
                .status(status)
                .build();
    }

    public AccountResponse updateAccountById(String id, AccountRequest accountRequest) {
        log.info("Before Update Account By Id: {} | {}", id, accountRequest);
        AccountResponse accountResponse;
        Status status;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            Bson updates = Updates.combine(List.of(
                    Updates.set("refAccountType._id", new ObjectId(accountRequest.getTypeId())),
                    Updates.set("refBank._id", new ObjectId(accountRequest.getBankId())),
                    Updates.set("description", accountRequest.getDescription()),
                    Updates.set("openingBalance", accountRequest.getOpeningBalance()),
                    Updates.set("status", accountRequest.getStatus()),
                    Updates.set("lastModified", LocalDateTime.now().toString())));

            long modifiedCount = new AccountDao().updateAccountById(filter, updates);

            if (modifiedCount > 0) {
                accountResponse = getAccountById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_ACCOUNT)
                        .build();
                accountResponse = AccountResponse.builder()
                        .accounts(Collections.emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Update Account By Id: {} | {}", id, accountRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_ACCOUNT)
                    .message(ex.toString())
                    .build();
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(status)
                    .build();
        }

        log.info("After Update Account By Id: {} | user: {}", id, accountResponse);
        return accountResponse;
    }

    public AccountResponse deleteAccountById(String id) {
        log.info("Before Delete Account By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            deleteCount = new AccountDao().deleteAccountById(filter);
        } catch (Exception ex) {
            log.error("Delete Account By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_ACCOUNT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete Account By Id: {} | deleteCount: {}", id, deleteCount);
        return AccountResponse.builder()
                .accounts(Collections.emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
