INSERT INTO PERSONA (id,nombre, genero, edad, identificacion, direccion, telefono)
VALUES
    (1,'Jose Lema', 'MASCULINO', null, null, 'Otavalo sn y principal', '098254785'),
    (2,'Marianela Montalvo', 'FEMENINO', null, null, 'Amazonas y NNUU', '097548965'),
    (3,'Juan Osorio', 'MASCULINO', null, null, '13 junio y Equinoccial', '098874587');

INSERT INTO CLIENTE (cliente_id, contrasenia, estado,id) VALUES
                                                                     (1, '1234', true,1),
                                                                     (2, '5678', true,2),
                                                                     (3, '1245', true,3);


INSERT INTO CUENTA (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
VALUES
    ('478758', 'AHORRO', 2000, true, 1),
    ('225487', 'CORRIENTE', 100, true, 2),
    ('495878', 'AHORRO', 0, true, 3),
    ('496825', 'AHORRO', 540, true, 2),
    ('585545', 'CORRIENTE', 100, true, 1);



INSERT INTO MOVIMIENTO (id,fecha, tipo_movimiento, valor, saldo, numero_cuenta)
VALUES
    (1,'2022-02-10', 'RETIRO', 575, 1425, '585545');

CREATE SEQUENCE if not exists persona_id_seq START WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM persona);
ALTER TABLE persona ALTER COLUMN id SET DEFAULT NEXTVAL('persona_id_seq');

CREATE SEQUENCE if not exists  movimiento_id_seq START WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM movimiento);
ALTER TABLE movimiento ALTER COLUMN id SET DEFAULT NEXTVAL('movimiento_id_seq');
