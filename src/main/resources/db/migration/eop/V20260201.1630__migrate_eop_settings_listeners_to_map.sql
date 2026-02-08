-- V20260201.1630__migrate_eop_settings_listeners_to_map.sql
-- 将 eop_app 表中 settings 字段里的 listeners 从列表格式转换为以 protocol 为 key 的映射格式

UPDATE eop_app
SET settings = jsonb_set(
    settings,
    '{listeners}',
    (
        SELECT jsonb_object_agg(elem->>'protocol', elem)
        FROM jsonb_array_elements(settings->'listeners') AS elem
    )
)
WHERE jsonb_typeof(settings->'listeners') = 'array';
