insert INTO customer(id, name, surname, citizenship, version, status)
values ('123e4567-e89b-12d3-a456-426614174000', 'test', 'tester', 'CZ', 0, 'ACTIVE');

insert INTO bank_account(account_number, bank_code, account_type, iban, balance, currency, version, status,
                                owner_id)
values ('abc123', '0200', 'PERSONAL', null, 1000, 'EUR', 0, 'ACTIVE', '123e4567-e89b-12d3-a456-426614174000');

insert into transaction(id, account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('234e4567-e89a-12d3-a456-426614174432', 'abc123', 'INCOMING_PAYMENT', '2022-11-20T10:32:00.896+02:00', 22.44,
        'EUR', 'asd234', null, 'COUNTED', 0);
insert into transaction(id, account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('aa4bedc5-2d8f-4f0c-b4a4-bb6dab48ce4d', 'abc123', 'INCOMING_PAYMENT', '2022-11-20T10:33:00.111+02:00', 2.05,
        'EUR', null, null, 'COUNTED', 0);
insert into transaction(id, account_number, transaction_type, value_date, amount, currency, variable_symbol,
                               specific_symbol, status, version)
values ('26351c92-e688-476b-997a-46f5e5097c5d', 'abc123', 'OUTGOING_PAYMENT', '2022-11-20T10:34:00.000+02:00', 13.95,
        'EUR', 'tre654', null, 'COUNTED', 0);