# Expose the ID so Jenkins rules can refer to this security group as a source.
output "security_group_id" {
  value       = aws_security_group.sonarqube.id
  description = "ID of the Sonarqube security group"
}