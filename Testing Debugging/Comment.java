public class Comment {
    private String author;
    private String text;
    private int rating;
    private int upvotes;
    private int downvotes;

    public Comment(String author, String text, int rating) {
        this.author = author;
        this.text = text;
        this.rating = rating;
        this.upvotes = 0;
        this.downvotes = 0;
    }
   
    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getVoteCount() {
        return upvotes - downvotes;
    }

    public void upvote() {
        upvotes++;
    }

    public void downvote() {
        downvotes++;
    }

    public String getFullDetails() {
        return "Author: " + author + "\n" +
               "Rating: " + rating + "\n" +
               "Comment: " + text + "\n" +
               "Upvotes: " + upvotes + "  Downvotes: " + downvotes;
    }
}