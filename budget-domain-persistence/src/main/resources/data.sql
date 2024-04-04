INSERT INTO ACCOUNT(id, account_id, password, nickname, status, `role`, sign_up_date_time)
VALUES (1, 'ADMIN', '{bcrypt}$2a$10$2camd68mgS0sUMvQoP/Xc.0ev5REbEGkqK9w5Gr1pDNwPWBzPk.lK', 'ADMIN', 'LIVED', 'ROLE_ADMIN', now());

INSERT INTO CATEGORY(id, `name`)
VALUES (1, '식비'),
       (2, '교통'),
       (3, '주거'),
       (4, '문화/여가'),
       (5, '카페'),
       (6, '여행'),
       (7, '교육'),
       (8, '기타');