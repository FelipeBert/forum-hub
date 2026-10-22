// Select the latest matching Ubuntu 22.04 image from Canonical's AWS account.
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

// Run SonarQube on a separate EC2 instance; its security group controls
// which Jenkins and administrator sources can reach the service.
resource "aws_instance" "sonarqube" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.sonarqube.id]
  subnet_id              = var.subnet_id

  tags = {
    Name    = "Jenkins-Sonarqube"
    Project = "forum-hub"
  }
}

resource "aws_ec2_instance_state" "sonarqube-state" {
  instance_id = aws_instance.sonarqube.id
  state       = "running"
}