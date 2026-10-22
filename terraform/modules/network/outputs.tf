# Export identifiers consumed by the application infrastructure modules.
output "vpc_id" {
  description = "ID of the VPC."
  value       = aws_vpc.main.id
}

output "public_subnets_ids" {
  description = "IDs of the public subnets."
  value       = aws_subnet.public[*].id
}

output "private_subnets_ids" {
  description = "IDs of the private subnets."
  value       = aws_subnet.private[*].id
}

output "private_route_table_id" {
  description = "ID of the route table used by private subnets."
  value       = aws_route_table.private.id
}
