# Export the controller's addresses and security-group ID for operations and
# other modules that need to establish narrowly scoped network rules.
output "private_ip" {
  value       = aws_instance.master.private_ip
  description = "Private IP of the Master for communication with agents"
}

output "public_ip" {
  value       = aws_instance.master.public_ip
  description = "Public IP for UI access"
}

output "security_group_id" {
  value       = aws_security_group.master-sg.id
  description = "ID of the Master security group"
}