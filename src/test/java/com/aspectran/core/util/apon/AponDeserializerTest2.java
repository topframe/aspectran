/**
 * Copyright 2008-2016 Juho Jeong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.core.util.apon;

public class AponDeserializerTest2 {

	public static void main(String argv[]) {
		try {
			Customer customer = new Customer();
			customer.putValue(Customer.id, "guest");
			customer.putValue(Customer.name, "Guest");
			customer.putValue(Customer.age, 20);
			customer.putValue(Customer.episode, "His individual skills are outstanding.\nI don't know as how he is handsome.");
			customer.putValue(Customer.approved, false);
			
			String text = AponSerializer.serialize(customer);
			
			Customer customer2 = new Customer();
			customer2 = AponDeserializer.deserialize(text, customer2);
			
			String text2 = AponSerializer.serialize(customer2);
			
			System.out.println("---------------------------------------------------");
			System.out.print(text);
			System.out.println("---------------------------------------------------");
			System.out.print(text2);
			System.out.println("---------------------------------------------------");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
