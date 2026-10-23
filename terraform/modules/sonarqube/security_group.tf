# A security group is a stateful virtual firewall attached to the supplied VPC.
resource "aws_security_group" "sonarqube" {
  name = "sonarqube"
  # This description is informational; the explicit rules below determine access.
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = var.vpc_id

  # Tags help identify and organize resources in the AWS console and tooling.
  tags = {
    Name = "Sonarqube-SG"
  }
}

# Allow instances in the agent security group to reach the SonarQube service
# on TCP port 9000.
resource "aws_vpc_security_group_ingress_rule" "tcp_from_agent_sg" {
  security_group_id = aws_security_group.sonarqube.id
  # Use the agent security group as the source allowed to communicate with
  # SonarQube.
  referenced_security_group_id = var.agent_sg
  from_port                    = 9000
  ip_protocol                  = "tcp"
  to_port                      = 9000
}

resource "aws_vpc_security_group_ingress_rule" "tcp_from_my_ip" {
  security_group_id = aws_security_group.sonarqube.id
  # /32 limits access to one IPv4 address; this rule exposes the SonarQube
  # service port only to that address.
  cidr_ipv4   = "${var.ip_admin}/32"
  from_port   = 9000
  ip_protocol = "tcp"
  to_port     = 9000
}


# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv4" {
  security_group_id = aws_security_group.sonarqube.id
  cidr_ipv4         = "0.0.0.0/0"
  # Protocol -1 allows all protocols and ports to all IPv4 destinations.
  ip_protocol = "-1"
}

# Egress rules control connections initiated from resources in this group.
resource "aws_vpc_security_group_egress_rule" "allow_all_outbound_ipv6" {
  security_group_id = aws_security_group.sonarqube.id
  cidr_ipv6         = "::/0"
  # Protocol -1 allows all protocols and ports to all IPv6 destinations.
  ip_protocol = "-1"
}