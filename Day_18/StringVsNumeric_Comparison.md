# 📘 String vs Numeric Comparison in MySQL
## 1. String Comparison in SQL

Strings are compared lexicographically (dictionary order).

Example:

SELECT * FROM products WHERE product_name > 'Mouse';

This will fetch words that come after "Mouse" alphabetically.

Example results: Phone, Tablet (since P and T come after M in dictionary order).

Words like Laptop, Keyboard, Headphones are excluded (since L, K, H come before M).

 If the string was 'Mz', it would still count as greater than 'Mouse' (dictionary-like comparison).

 By default: String comparison is case-insensitive.

To force case-sensitive comparison, use the BINARY keyword:

SELECT * FROM products WHERE BINARY product_name > 'Mouse';
## 2. Using SELECT as Expression Evaluator

You can use SELECT without a table, just to test expressions.

Example:

SELECT '1' < '2';

Output → 1 (True).

Why? Because MySQL compared '1' and '2' as strings (not numbers).

'1' → ASCII = 49

'2' → ASCII = 50

Since 49 < 50 → result is TRUE (1).

 Only the first differing character is checked. If same, then it checks the next character.

## 3. ASCII Values

Every character has a corresponding ASCII code.

'1' → 49

'2' → 50

This is why MySQL treats them as characters during comparison.

## 4. Forcing Numeric Comparison

Add +0 to force MySQL to treat a string as a number.

SELECT '100' + 0 < '2' + 0;

Output → 0 (False) because now it compares numerically → 100 < 2 (False).

Example:

SELECT 100 < '211fcfc';

MySQL reads string from left to right and stops when a non-digit is found.

'211fcfc' → becomes 211.

Comparison → 100 < 211 → 1 (True).

 If the string starts with alphabets (no digit at beginning), it is treated as 0.

## 5. Key Takeaways

String comparison = lexicographic (dictionary-like).

Numeric vs String comparison = MySQL tries to convert string to number.

Use +0 for numeric conversion.

Use BINARY for case-sensitive string comparison.

Remember ASCII values decide ordering in string comparison.