# A security group is a stateful virtual firewall attached to the supplied VPC.
resource "aws_security_group" "rds-sg" {
  name = "rds-sg"
  # This description is informational; the explicit rules below determine access.
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = var.vpc_id

  # Tags help identify and organize resources in the AWS console and tooling.
  tags = {
    Name = "rds-sg"
  }
}

# Ingress rules control connections initiated toward resources in this group.
resource "aws_vpc_security_group_ingress_rule" "tcp_from_agent_sg" {
  security_group_id = aws_security_group.rds-sg.id
  # Referencing the controller security group limits SSH to Jenkins master
  # instances without hard-coding their changing private IP addresses.
  referenced_security_group_id = var.agent_sg
  from_port                    = 3306
  # TCP port 22 is conventionally used for SSH.
  ip_protocol = "tcp"
  to_port     = 3306
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv4" {
  security_group_id = aws_security_group.rds-sg.id
  cidr_ipv4         = "0.0.0.0/0"
  # Protocol -1 allows all protocols and ports to all IPv4 destinations.
  ip_protocol = "-1"
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv6" {
  security_group_id = aws_security_group.rds-sg.id
  cidr_ipv6         = "::/0"
  # Protocol -1 allows all protocols and ports to all IPv6 destinations.
  ip_protocol = "-1"
}