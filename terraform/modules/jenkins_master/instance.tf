// Select the latest matching Ubuntu 22.04 image from Canonical's AWS account.
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

// Place the Jenkins controller in the subnet supplied by the root module.
resource "aws_instance" "master" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  vpc_security_group_ids = [aws_security_group.master-sg.id]
  subnet_id              = var.subnet_id

  tags = {
    Name    = "Jenkins-Master"
    Project = "forum-hub"
  }
}

resource "aws_ec2_instance_state" "master-state" {
  instance_id = aws_instance.master.id
  state       = "running"
}