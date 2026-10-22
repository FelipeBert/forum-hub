variable "agent_subnet_id" {
  description = "ID of the subnet where the RDS agent will be deployed."
  type        = list(string)
}

variable "agent_sg" {
  description = "IP of the Agent Security Group"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC where the RDS will be provisioned"
  type        = string
}