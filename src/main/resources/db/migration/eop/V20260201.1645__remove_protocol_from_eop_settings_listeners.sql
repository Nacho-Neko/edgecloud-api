-- V20260201.1645__remove_protocol_from_eop_settings_listeners.sql
-- 从 eop_app.settings.listeners 的各个对象中删除 protocol 字段，因为 protocol 已经是 map 的 key

UPDATE eop_app
SET settings = jsonb_set(
    settings,
    '{listeners}',
    (
        SELECT jsonb_object_agg(key, value - 'protocol')
        FROM jsonb_each(settings->'listeners')
    )
)
WHERE settings ? 'listeners' AND settings->'listeners' IS NOT NULL;
