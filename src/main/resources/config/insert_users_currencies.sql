MERGE INTO users(id, name, gender) VALUES(1, 'Jeremy', 'm');
MERGE INTO users(id, name, gender) VALUES(2, 'Chloe', 'f');

MERGE INTO currency(id, name, shortname, icon) VALUES(1, 'Euro', 'EUR', 'no');
MERGE INTO currency(id, name, shortname, icon) VALUES(2, 'Franc suisse', 'CHF', 'no');