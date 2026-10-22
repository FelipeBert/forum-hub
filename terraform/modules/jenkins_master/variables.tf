# Inputs connect this module to the VPC, chosen subnet, and SonarQube network
# rule that permits webhook traffic back to Jenkins.
variable "vpc_id" {
  description = "ID of the VPC where the Jenkins Master will be provisioned"
  type        = string
}

variable "subnet_id" {
  description = "ID of the public subnet for the Master"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "sonarqube_sg" {
  description = "Sonarqube Security Group id"
  type        = string
}

variable "ip_admin" {
  description = "Administrator IP for SSH/UI access"
  type        = string
}