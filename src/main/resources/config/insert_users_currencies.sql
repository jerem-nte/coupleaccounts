MERGE INTO users(id, name, gender) VALUES(1, 'Jeremy', 'm');
MERGE INTO users(id, name, gender) VALUES(2, 'Chloe', 'f');

MERGE INTO currency(id, name, shortname, icon) VALUES(1, 'Franc suisse', 'CHF', 'no');
MERGE INTO currency(id, name, shortname, icon) VALUES(2, 'Euro', 'EUR', 'no');