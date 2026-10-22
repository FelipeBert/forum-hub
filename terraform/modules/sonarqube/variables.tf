# The root module passes the VPC, subnet, and Jenkins-agent security group so
# SonarQube can be deployed with narrowly scoped network access.
variable "vpc_id" {
  description = "ID of the VPC where the Sonarqube will be provisioned"
  type        = string
}

variable "subnet_id" {
  description = "ID of the public subnet for the Sonarqube"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "agent_sg" {
  description = "IP of the Agent Security Group"
  type        = string
}

variable "ip_admin" {
  description = "Administrator IP for SSH/UI access"
  type        = string
}