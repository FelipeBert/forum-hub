// Root module: wires reusable infrastructure modules together. Values exported
// by one module (such as subnet IDs) become inputs to another.
module "network" {
  source = "./modules/network"

  vpc_cidr             = var.vpc_cidr
  public_subnets_cidr  = var.public_subnets_cidr
  private_subnets_cidr = var.private_subnets_cidr
}

module "jenkins_master" {
  source = "./modules/jenkins_master"

  vpc_id       = module.network.vpc_id
  subnet_id    = module.network.public_subnets_ids[0]
  sonarqube_sg = module.sonarqube.security_group_id

  ip_admin  = var.ip_admin
}

module "jenkins_agents" {
  source = "./modules/jenkins_agent"

  vpc_id    = module.network.vpc_id
  subnet_id = module.network.private_subnets_ids[0]

  master_sg  = module.jenkins_master.security_group_id
  bucket_arn = module.s3.bucket_arn
}

module "sonarqube" {
  source = "./modules/sonarqube"

  vpc_id    = module.network.vpc_id
  subnet_id = module.network.private_subnets_ids[1]

  agent_sg = module.jenkins_agents.security_group_id

  ip_admin  = var.ip_admin
}

module "s3" {
  source = "./modules/s3_bucket"
}

module "rds" {
  source = "./modules/rds"

  vpc_id          = module.network.vpc_id
  agent_sg        = module.jenkins_agents.security_group_id
  agent_subnet_id = module.network.private_subnets_ids
}