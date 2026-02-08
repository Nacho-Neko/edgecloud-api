-- V20260125.1555__add_product_desc_and_intro.sql
ALTER TABLE eop_product ADD COLUMN description VARCHAR(255);
ALTER TABLE eop_product ADD COLUMN introduction TEXT;
