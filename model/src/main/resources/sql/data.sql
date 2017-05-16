INSERT INTO "customer" ("name", "address", "birth_date")
  VALUES ('František Maša', 'Náměství svobody 666', '1995-10-29');

INSERT INTO "customer" ("name", "address", "birth_date")
VALUES ('Michal Šustera', 'Cejl 666', '1996-04-11');

INSERT INTO "room" ("number", "floor", "capacity", "price_per_day")
    VALUES
      (1, 1, 3, 200),
      (2, 1, 3, 200),
      (3, 1, 2, 150),
      (4, 2, 5, 300),
      (29, 10, 2, 1500);

INSERT INTO "reservation" ("customer_id", "room_id", "since", "until")
    VALUES (1, 1, '2017-05-31', '2017-06-04');