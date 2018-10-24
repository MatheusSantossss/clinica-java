DROP SCHEMA clinica;
CREATE SCHEMA IF NOT EXISTS clinica;
use clinica;
	
CREATE TABLE historico (
  idhistorico INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nome VARCHAR(30) NOT NULL,
  rg VARCHAR(20) NOT NULL,
  placa VARCHAR(10)  NULL,
  hora_entrada VARCHAR(50) NOT NULL,
  motivo VARCHAR(15) NOT NULL,
  hora_saida VARCHAR(50) NOT NULL,
  PRIMARY KEY(idhistorico)
);

CREATE TABLE idoso (
  
  id_idoso INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT,
  nome_idoso VARCHAR(50) NOT NULL,
  idade_idoso VARCHAR(10) NOT NULL,
  rg_idoso VARCHAR(12) NOT NULL,
  medicamento_idoso VARCHAR(50) NOT NULL,
  situacao_idoso VARCHAR(200) NOT NULL,
  PRIMARY KEY(id_idoso)
  
);

CREATE TABLE idoso_has_visitante (
  idoso_rg_idoso VARCHAR(12),
  visitante_idvisitante INTEGER UNSIGNED AUTO_INCREMENT,
  PRIMARY KEY(idoso_rg_idoso, visitante_idvisitante),
  INDEX idoso_has_visitante_FKIndex1(idoso_rg_idoso),
  INDEX idoso_has_visitante_FKIndex2(visitante_idvisitante)
);

CREATE TABLE responsavel (
  idiresponsavel INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nome_responsavel VARCHAR(50) NOT NULL,
  idade_responsavel VARCHAR(3) NOT NULL,
  rg_responsavel VARCHAR(15) NOT NULL,
  telefone_responsavel VARCHAR(20) NOT NULL,
  PRIMARY KEY(idiresponsavel)
);

CREATE TABLE usuario (
  idusuario INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nome_usuario VARCHAR(50) NOT NULL,
  matricula_usuario VARCHAR(30)  NOT NULL,
  senha_usuario VARCHAR(30)  NOT NULL,
  cargo_usuario VARCHAR(25)NOT NULL,
  chave_gerente VARCHAR(20) NULL,
  PRIMARY KEY(idusuario)
);

CREATE TABLE visitante (
  idvisitante INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  nome_visitante VARCHAR(50) NOT NULL,
  telefone_visitante VARCHAR(15) NOT NULL,
  rg_visitante VARCHAR(15)  NOT NULL,
  PRIMARY KEY(idvisitante)
);








    