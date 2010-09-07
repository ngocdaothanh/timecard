class User
  include SimplyStored::Couch

  property :username
  property :name
  property :encrypted_password
  property :manager_id
  property :disabled, :type => :boolean, :default => false

#  has_many :posts
  belongs_to :group
  has_many :timeentries

  attr_accessor :password
#  attr_accessible :username, :name , :manager_id, :disabled, :password, :group

  validates_presence_of :username, :name

  def manager_name
    #puts 'TTTTTTT #{self.manager_id}'
    User.find(self.manager_id).name unless self.manager_id.nil?
  end


  before_save :encrypt_password

  private
    def encrypt_password
      self.encrypted_password = secure_hash(password) unless password.nil?
    end

    def secure_hash(string)
      string ||= ""
      Digest::SHA2.hexdigest(string)
    end

  
end
