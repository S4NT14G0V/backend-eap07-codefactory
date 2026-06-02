-- ===============================================
-- Procedimiento Almacenado: sp_export_transaction_summary
-- ===============================================
-- Propósito: Obtiene un resumen rápido de las transacciones de un comercio
-- en un período dado, incluyendo conteo total y monto acumulado.
-- Se usa como paso previo a la exportación para validar el volumen.
--
-- Uso:
--   CALL sp_export_transaction_summary('merchant_123', '2026-01-01 00:00:00', '2026-02-01 00:00:00', @total, @amount);
--   SELECT @total, @amount;
-- ===============================================

DELIMITER //

CREATE PROCEDURE sp_export_transaction_summary(
    IN p_merchant_id VARCHAR(255),
    IN p_from DATETIME,
    IN p_to DATETIME,
    OUT p_total_count BIGINT,
    OUT p_total_amount DECIMAL(19,2)
)
BEGIN
    SELECT COUNT(*), COALESCE(SUM(amount), 0)
    INTO p_total_count, p_total_amount
    FROM transactions
    WHERE merchant_id = p_merchant_id
      AND created_at >= p_from
      AND created_at < p_to;
END //

DELIMITER ;
