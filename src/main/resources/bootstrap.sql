INSERT INTO referencedata.users (id, username, firstName, lastName, email, timezone) VALUES ('35316636-6264-6331-2d34-3933322d3462', 'admin', 'Admin', 'User', 'example@mail.com', 'UTC');

INSERT INTO referencedata.rights (id, name, type) VALUES ('e96017ff-af8c-4313-a070-caa70465c949', 'FACILITIES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('4e731cf7-854f-4af7-9ea4-bd5d8ed7bb22', 'GEOGRAPHIC_ZONES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('5c4b3b9b-713e-4b9a-8c58-7efcd2954512', 'SUPERVISORY_NODES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('fb6a0053-6254-4b41-8028-bf91421f90dd', 'PRODUCTS_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('8816edba-b8a9-11e6-80f5-76304dec7eb7', 'REQUISITION_TEMPLATES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('4bed4f40-36b5-42a7-94c9-0fd3d4252374', 'STOCK_CARD_TEMPLATES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('ebad51c3-f7c3-4fab-97e1-839973b045d4', 'USER_ROLES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('42791f7a-84a1-470b-bc3c-4160bc99f13c', 'PROCESSING_SCHEDULES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('3687ea98-8a1e-4347-984c-3fd97d072066', 'USERS_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('9ade922b-3523-4582-bef4-a47701f7df14', 'REQUISITION_CREATE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('bffa2de2-dc2a-47dd-b126-6501748ac3fc', 'REQUISITION_APPROVE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('feb4c0b8-f6d2-4289-b29d-811c1d0b2863', 'REQUISITION_AUTHORIZE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('c3eb5df0-c3ac-4e70-a978-02827462f60e', 'REQUISITION_DELETE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('e101d2b8-6a0f-4af6-a5de-a9576b4ebc50', 'REQUISITION_VIEW', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('7958129d-c4c0-4294-a40c-c2b07cb8e515', 'REQUISITION_CONVERT_TO_ORDER', 'ORDER_FULFILLMENT');
INSERT INTO referencedata.rights (id, name, type) VALUES ('65626c3d-513f-4255-93fd-808709860594', 'FULFILLMENT_TRANSFER_ORDER', 'ORDER_FULFILLMENT');
INSERT INTO referencedata.rights (id, name, type) VALUES ('24df2715-850c-4336-b650-90eb78c544bf', 'PODS_MANAGE', 'ORDER_FULFILLMENT');
