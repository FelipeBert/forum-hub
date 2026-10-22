# Expose the worker's private address and security-group ID for operations
# and for other modules that need source-restricted network rules.
output "agent_private_ip" {
  value       = aws_instance.agent.private_ip
  description = "Private IP of the Agent for communication with other services"
}

output "security_group_id" {
  value       = aws_security_group.agent-sg.id
  description = "ID of the Agent's security group"
}