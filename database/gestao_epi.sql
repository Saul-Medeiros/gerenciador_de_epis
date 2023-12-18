-- MySQL Script generated by MySQL Workbench
-- Thu Nov  2 22:33:23 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gestao_epi
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gestao_epi
-- -----------------------------------------------------
DROP DATABASE IF EXISTS gestao_epi;
CREATE SCHEMA IF NOT EXISTS `gestao_epi` DEFAULT CHARACTER SET utf8;
USE `gestao_epi` ;

-- -----------------------------------------------------
-- Table `gestao_epi`.`perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`perfil` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(64) NOT NULL,
  `data_cadastro` DATE NOT NULL,
  `status` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(128) NOT NULL,
  `login` VARCHAR(24) NOT NULL,
  `senha` VARCHAR(254) NOT NULL,
  `status` INT NOT NULL,
  `id_perfil` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `senha_UNIQUE` (`senha` ASC),
  INDEX `fk_usuario_perfil_idx` (`id_perfil` ASC),
  CONSTRAINT `fk_usuario_perfil`
    FOREIGN KEY (`id_perfil`)
    REFERENCES `gestao_epi`.`perfil` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`menu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`menu` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(64) NOT NULL,
  `link` VARCHAR(128) NOT NULL,
  `icone` VARCHAR(64) NOT NULL,
  `exibir` INT NOT NULL,
  `status` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`menu_perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`menu_perfil` (
  `id_menu` INT NOT NULL,
  `id_perfil` INT NOT NULL,
  PRIMARY KEY (`id_menu`, `id_perfil`),
  INDEX `fk_menu_has_perfil_perfil1_idx` (`id_perfil` ASC),
  INDEX `fk_menu_has_perfil_menu1_idx` (`id_menu` ASC),
  CONSTRAINT `fk_menu_has_perfil_menu1`
    FOREIGN KEY (`id_menu`)
    REFERENCES `gestao_epi`.`menu` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_has_perfil_perfil1`
    FOREIGN KEY (`id_perfil`)
    REFERENCES `gestao_epi`.`perfil` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`cargo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`cargo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(64) NOT NULL,
  `funcionarios_vinculados` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `nome_UNIQUE` (`nome` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`funcionario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`funcionario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(64) NOT NULL,
  `setor` VARCHAR(100) NOT NULL,
  `id_cargo` INT NOT NULL,
  `status` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_funcionario_cargo1_idx` (`id_cargo` ASC),
  CONSTRAINT `fk_funcionario_cargo1`
    FOREIGN KEY (`id_cargo`)
    REFERENCES `gestao_epi`.`cargo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`epi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`epi` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(64) NOT NULL,
  `descricao` VARCHAR(100) NULL,
  `status` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`alocacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`alocacao` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `data_entrega` DATE NOT NULL,
  `data_devolucao` DATE NULL,
  `id_usuario` INT NOT NULL,
  `id_funcionario` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_alocacao_usuario1_idx` (`id_usuario` ASC),
  INDEX `fk_alocacao_Funcionario1_idx` (`id_funcionario` ASC),
  CONSTRAINT `fk_alocacao_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `gestao_epi`.`usuario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_alocacao_Funcionario1`
    FOREIGN KEY (`id_funcionario`)
    REFERENCES `gestao_epi`.`funcionario` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`alocacao_epi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`alocacao_epi` (
  `id_alocacao` INT NOT NULL,
  `id_epi` INT NOT NULL,
  PRIMARY KEY (`id_alocacao`, `id_epi`),
  INDEX `fk_alocacao_has_epi_epi1_idx` (`id_epi` ASC),
  INDEX `fk_alocacao_has_epi_alocacao1_idx` (`id_alocacao` ASC),
  CONSTRAINT `fk_alocacao_has_epi_alocacao1`
    FOREIGN KEY (`id_alocacao`)
    REFERENCES `gestao_epi`.`alocacao` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_alocacao_has_epi_epi1`
    FOREIGN KEY (`id_epi`)
    REFERENCES `gestao_epi`.`epi` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gestao_epi`.`cargo_epi`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gestao_epi`.`cargo_epi` (
  `id_cargo` INT NOT NULL,
  `id_epi` INT NOT NULL,
  PRIMARY KEY (`id_cargo`, `id_epi`),
  INDEX `fk_cargo_has_epi_epi1_idx` (`id_epi` ASC),
  INDEX `fk_cargo_has_epi_cargo1_idx` (`id_cargo` ASC),
  CONSTRAINT `fk_cargo_has_epi_cargo1`
    FOREIGN KEY (`id_cargo`)
    REFERENCES `gestao_epi`.`cargo` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_cargo_has_epi_epi1`
    FOREIGN KEY (`id_epi`)
    REFERENCES `gestao_epi`.`epi` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;