insert INTO customer(id, name, surname, citizenship, version, status)
values ('f786717a-7edc-49ff-98b5-1861cba963bd', 'test', 'tester', 'CZ', 0, 'ACTIVE');
insert INTO customer(id, name, surname, citizenship, version, status)
values ('82caf85d-50ef-4d1a-a061-290b2a6fc962', 'test', 'anotherTester', 'AT', 0, 'INACTIVE');


insert INTO bank_account(account_number, bank_code, account_type, iban, balance, currency, version, status,
                                owner_id)
values ('dfg456', '0200', 'PERSONAL', null, 1000, 'EUR', 0, 'ACTIVE', 'f786717a-7edc-49ff-98b5-1861cba963bd');
insert INTO bank_account(account_number, bank_code, account_type, iban, balance, currency, version, status,
                                owner_id)
values ('cvb456', '0200', 'BUSINESS', null, 10000, 'EUR', 0, 'INACTIVE', 'f786717a-7edc-49ff-98b5-1861cba963bd');




insert into transaction(id, account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('eb3db211-67f1-4dbb-9d01-ae421e6a4e4a', 'dfg456', 'INCOMING_PAYMENT', '2022-11-20T10:32:00.896+02:00', 222,
        'EUR', 'asd234', null, 'COUNTED', 0);
insert into transaction(id, account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('6ce00851-b52b-4465-a32c-74286be1be9d', 'dfg456', 'INCOMING_PAYMENT', '2022-11-20T10:33:00.111+02:00', 444,
        'EUR', null, null, 'COUNTED', 0);