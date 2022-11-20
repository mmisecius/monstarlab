insert INTO customer(id, name, surname, citizenship, version, status)
values ('123e4567-e89b-12d3-a456-426614174000', 'test', 'tester', 'CZ', 0, 'ACTIVE');

insert INTO bank_account(account_number, bank_code, account_type, iban, balance, currency, version, status,
                                owner_id)
values ('abc123', '0200', 'PERSONAL', null, 1000, 'EUR', 0, 'ACTIVE', '123e4567-e89b-12d3-a456-426614174000');

insert INTO bank_account(account_number, bank_code, account_type, iban, balance, currency, version, status,
                                owner_id)
values ('bcd234', '0500', 'BUSINESS', null, 100, 'EUR', 0, 'ACTIVE', '123e4567-e89b-12d3-a456-426614174000');

insert into transaction(id, account_number, source_account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('234e4567-e89a-12d3-a456-426614174432', 'abc123', 'bcd234','INCOMING_PAYMENT', '2022-11-20T10:32:00.896+02:00', 500,
        'EUR', 'asd234', null, 'COUNTED', 0);
insert into transaction(id, account_number, target_account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('aa4bedc5-2d8f-4f0c-b4a4-bb6dab48ce4d', 'bcd234', 'abc123', 'OUTGOING_PAYMENT', '2022-11-20T10:33:00.111+02:00', 500,
        'EUR', null, null, 'COUNTED', 0);