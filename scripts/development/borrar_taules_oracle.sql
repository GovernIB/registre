BEGIN
  FOR c IN (SELECT table_name, constraint_name FROM user_constraints WHERE constraint_type = 'R')
  LOOP
    EXECUTE IMMEDIATE ('alter table ' || c.table_name || ' drop constraint ' || c.constraint_name);
  END LOOP;
  FOR c IN (SELECT table_name FROM user_tables)
  LOOP
    EXECUTE IMMEDIATE ('drop table ' || c.table_name);
  END LOOP;
END;