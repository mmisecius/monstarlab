CREATE TABLE customer
(
    id          varchar(36),
    name        character varying not null,
    surname     character varying not null,
    citizenship varchar(2)        not null,
    version     int               not null,
    status      varchar(20)       not null,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE TABLE bank_account
(
    account_number varchar(36),
    bank_code      varchar(10) not null,
    account_type   varchar(10) not null,
    iban           varchar(34),
    balance        decimal     not null,
    currency       varchar(3)  not null,
    version        int         not null,
    status         varchar(20) not null,
    owner_id       varchar(36) not null,
    CONSTRAINT bank_account_pk PRIMARY KEY (account_number),
    CONSTRAINT fk_bank_account_customer FOREIGN KEY (owner_id) REFERENCES customer (id)
);

CREATE TABLE transaction
(
    id               varchar(36),
    account_number   varchar(36),
    transaction_type varchar(20) not null,
    value_date       timestamp with time zone,
    amount           decimal     not null,
    currency         varchar(3)  not null,
    variable_symbol  character varying,
    specific_symbol   character varying,
    status           varchar(20) not null,
    version          int         not null,
    CONSTRAINT transaction_pk PRIMARY KEY (id),
    CONSTRAINT fk_transaction_bank_account FOREIGN KEY (account_number) REFERENCES bank_account (account_number)
);