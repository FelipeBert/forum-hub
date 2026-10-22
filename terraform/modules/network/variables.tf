# CIDR inputs let the root module define the network layout while keeping
# this module reusable for different VPC sizes.
variable "vpc_cidr" {
  description = "CIDR block for the VPC."
  type        = string
}

variable "public_subnets_cidr" {
  description = "CIDR blocks for the public subnets."
  type        = list(string)

  validation {
    condition     = length(var.public_subnets_cidr) > 0
    error_message = "At least one public subnet CIDR is required for the NAT gateway."
  }
}

variable "private_subnets_cidr" {
  description = "CIDR blocks for the private subnets."
  type        = list(string)
}