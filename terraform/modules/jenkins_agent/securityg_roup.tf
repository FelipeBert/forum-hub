# A security group is a stateful virtual firewall attached to the supplied VPC.
resource "aws_security_group" "agent-sg" {
  name = "agent-sg"
  # This description is informational; the explicit rules below determine access.
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = var.vpc_id

  # Tags help identify and organize resources in the AWS console and tooling.
  tags = {
    Name = "agent-sg"
  }
}

# Ingress rules control connections initiated toward resources in this group.
resource "aws_vpc_security_group_ingress_rule" "ssh_from_master" {
  security_group_id = aws_security_group.agent-sg.id
  # Referencing the controller security group limits SSH to Jenkins master
  # instances without hard-coding their changing private IP addresses.
  referenced_security_group_id = var.master_sg
  from_port                    = 22
  # TCP port 22 is conventionally used for SSH.
  ip_protocol = "tcp"
  to_port     = 22
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv4" {
  security_group_id = aws_security_group.agent-sg.id
  cidr_ipv4         = "0.0.0.0/0"
  # Protocol -1 allows all protocols and ports to all IPv4 destinations.
  ip_protocol = "-1"
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv6" {
  security_group_id = aws_security_group.agent-sg.id
  cidr_ipv6         = "::/0"
  # Protocol -1 allows all protocols and ports to all IPv6 destinations.
  ip_protocol = "-1"
}