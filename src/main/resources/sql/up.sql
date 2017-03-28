CREATE TABLE "customer" (
  "id" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  "name" VARCHAR(250) NOT NULL,
  "address" VARCHAR(250) NOT NULL,
  "birth_date" DATE NOT NULL
);

CREATE TABLE "room" (
  "id" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  "floor" INT NOT NULL,
  "number" BIGINT NOT NULL UNIQUE,
  "capacity" INT NOT NULL,
  "price_per_day" DECIMAL NOT NULL
);

CREATE TABLE "reservation" (
  "id" BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  "room_id" BIGINT NOT NULL,
  "customer_id" BIGINT NOT NULL UNIQUE,
  "since" DATE NOT NULL,
  "until" DATE NOT NULL
);
