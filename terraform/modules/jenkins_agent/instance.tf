// Select the latest matching Ubuntu 22.04 image from Canonical's AWS account.
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

// The worker is intended for a private subnet; its instance profile supplies
// AWS credentials without storing static access keys on the machine.
resource "aws_instance" "agent" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.agent-sg.id]
  subnet_id              = var.subnet_id
  iam_instance_profile   = aws_iam_instance_profile.ec2_s3_profile.name

  tags = {
    Name    = "Jenkins-Agent"
    Project = "forum-hub"
  }
}

resource "aws_ec2_instance_state" "agent-state" {
  instance_id = aws_instance.agent.id
  state       = "running"
}