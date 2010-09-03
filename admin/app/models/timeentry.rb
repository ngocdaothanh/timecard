class Timeentry
  include SimplyStored::Couch

  property :date, :type => Date
  property :arrive_at, :type => Time
  property :out_at, :type => Time
  property :return_at, :type => Time
  property :leave_at, :type => Time
  property :notes
  property :tcexception_id
  
  belongs_to :user
end
