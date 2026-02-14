ALTER TABLE eop_bound ADD COLUMN status INT NOT NULL DEFAULT 1;
COMMENT ON COLUMN eop_bound.status IS '状态: 1-运行, 2-停止, 3-暂停';
