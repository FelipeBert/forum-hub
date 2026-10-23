# A security group is a stateful virtual firewall attached to the supplied VPC.
resource "aws_security_group" "master-sg" {
  name = "master-sg"
  # This description is informational; the explicit rules below determine access.
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = var.vpc_id

  # Tags help identify and organize resources in the AWS console and tooling.
  tags = {
    Name = "master-sg"
  }
}

# Ingress rules control connections initiated toward resources in this group.
resource "aws_vpc_security_group_ingress_rule" "ssh_from_my_ip" {
  security_group_id = aws_security_group.master-sg.id
  # /32 denotes one IPv4 address, restricting SSH access to this source.
  # cidr_ipv4 expects one CIDR string (not a list) for this AWS resource.
  cidr_ipv4 = "${var.ip_admin}/32"
  from_port = 22
  # TCP port 22 is conventionally used for SSH.
  ip_protocol = "tcp"
  to_port     = 22
}

# This permits HTTP from any IPv4 address; consider narrowing it for production.
resource "aws_vpc_security_group_ingress_rule" "allow_http" {
  security_group_id = aws_security_group.master-sg.id
  # cidr_ipv4 expects a single CIDR string rather than a list.
  cidr_ipv4   = "0.0.0.0/0"
  from_port   = 80
  ip_protocol = "tcp"
  to_port     = 80
}

# Allow SonarQube to send the webhook analysis response to Jenkins over TCP port 8080.
resource "aws_vpc_security_group_ingress_rule" "allow_tcp_response_from_sonarqube" {
  security_group_id = aws_security_group.master-sg.id
  # Restrict this connection to resources in the SonarQube security group.
  referenced_security_group_id = var.sonarqube_sg
  from_port                    = 8080
  ip_protocol                  = "tcp"
  to_port                      = 8080
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv4" {
  security_group_id = aws_security_group.master-sg.id
  cidr_ipv4         = "0.0.0.0/0"
  # Protocol -1 allows all protocols and ports to all IPv4 destinations.
  ip_protocol = "-1"
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv6" {
  security_group_id = aws_security_group.master-sg.id
  cidr_ipv6         = "::/0"
  # Protocol -1 allows all protocols and ports to all IPv6 destinations.
  ip_protocol = "-1"
}