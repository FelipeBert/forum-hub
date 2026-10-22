# The root module supplies the network identifiers and S3 bucket scope so
# this worker module can be reused without hard-coding those dependencies.
variable "vpc_id" {
  description = "ID of the VPC where the Jenkins Agent will be provisioned"
  type        = string
}

variable "subnet_id" {
  description = "ID of the public subnet for the Agent"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.micro"
}

variable "master_sg" {
  description = "IP of the Master Security Group"
  type        = string
}

variable "bucket_arn" {
  description = "Bucket ARN"
  type        = string
}