class User
  include SimplyStored::Couch

  property :username
  property :name
  property :encrypted_password
  property :manager_id
  property :disabled, :type => :boolean, :default => false

#  has_many :posts
  belongs_to :group

  attr_accessor :password
#  attr_accessible :username, :name , :manager_id, :disabled, :password, :group

  validates_presence_of :username, :name, :password

  before_save :encrypt_password

  private
    def encrypt_password
      self.encrypted_password = secure_hash(password)
    end

    def secure_hash(string)
      string ||= ""
      Digest::SHA2.hexdigest(string)
    end

  
end
